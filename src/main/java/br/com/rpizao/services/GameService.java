package br.com.rpizao.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rpizao.converters.GameConverter;
import br.com.rpizao.converters.MovieConverter;
import br.com.rpizao.converters.RoundConverter;
import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Option;
import br.com.rpizao.dtos.Question;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Game;
import br.com.rpizao.entities.Movie;
import br.com.rpizao.entities.Round;
import br.com.rpizao.entities.Score;
import br.com.rpizao.entities.User;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.repositories.GameRepository;
import br.com.rpizao.services.interfaces.IGameService;
import br.com.rpizao.services.interfaces.IMovieService;
import br.com.rpizao.services.interfaces.IScoreService;
import br.com.rpizao.services.interfaces.IUserService;
import br.com.rpizao.utils.CryptoUtils;

@Service
public class GameService implements IGameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private IMovieService movieService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IScoreService scoreService;
	
	private static final MovieConverter movieConverter = new MovieConverter();
	private static final GameConverter gameConverter = new GameConverter();
	private static final RoundConverter roundConverter = new RoundConverter();
	

	@Override
	public Battle start(String userCode) throws BusinessException {
		User user = userService.findByCode(userCode);
		if(user == null) {
			throw new BusinessException("Código de usuário está em branco ou não existe.");
		}
		
		List<Round> rounds = new ArrayList<>();
		rounds.add(createRoundFirst());
		
		Game game = Game.builder()
			.code(CryptoUtils.randomString(5))
			.create(LocalDateTime.now())
			.rounds(rounds)
			.user(user) 
			.build();
			
		gameRepository.save(game);
		return gameConverter.convertFromDto(game);
	}
	
	@Override
	public Battle nextQuestion(String code) throws BusinessException {
		Game game = gameRepository.findByCode(code);
		if(game == null) {
			throw new BusinessException("Código de jogo está em branco ou não existe.");
		}
		
		final boolean answerIsPendent = game.getRounds().stream().anyMatch(r -> r.getCorrect() == null);
		if(answerIsPendent) {
			throw new BusinessException("Não é permitido ir para o quiz seguinte, antes de responder o atual.");
		}
		
		game.getRounds().add(createRound(game));
		gameRepository.save(game);
		
		return gameConverter.convertFromDto(game);
	}
	
	private Round createRoundFirst() throws BusinessException {
		return createRound(null);
	}
	
	private Round createRound(Game game) throws BusinessException {
		List<Movie> movies = loadingMoviesForRound(game);
		
		return Round.builder()
				.first(movies.get(0))
				.second(movies.get(1))
				.build();
	}

	private List<Movie> loadingMoviesForRound(Game game) {
		List<Movie> movies = movieService.loadingUniqueTupleOfMovies();
		boolean dubbed = areDubbedMoviesInTheGame(game, movies);
		if(dubbed) {
			return loadingMoviesForRound(game);
		}
		return movies;
	}
	
	/**
	 * Validando se os pares estão repetidos, independente da posição,
	 * nas rodadas anteriores.
	 * Por motivos óbvios, a regra só vale a partir da segunda rodada.
	 * 
	 * @param game
	 * @param movies
	 * @return TRUE se estiverem repetidos
	 */
	private boolean areDubbedMoviesInTheGame(Game game, List<Movie> movies) {
		if(game == null) return false;
		
		final Movie firstMovieNew = movies.get(0);
		final Movie secondMovieNew = movies.get(1);
		
		return game.getRounds().stream().anyMatch(r -> {
			final Movie firstMovie = r.getFirst();
			final Movie secondMovie = r.getSecond();
			return (firstMovie.equals(firstMovieNew) && secondMovie.equals(secondMovieNew)) 
						|| (firstMovie.equals(secondMovieNew) && secondMovie.equals(firstMovieNew));
		});
	}

	@Override
	public void finish(ScoreResult scoreResult) throws BusinessException {
		Game gameEnded = gameRepository.findByCode(scoreResult.getGameCode());
		if(gameEnded == null) {
			throw new BusinessException("Código de jogo está em branco ou não existe.");
		}
		
		if(checkIfExistsRoundPendent(gameEnded)) {
			throw new BusinessException("Não é possível encerrar um desafio que possua perguntas pendentes.");
		}
		
		gameEnded.setFinish(LocalDateTime.now());
		gameRepository.save(gameEnded);
		
		scoreService.publish(
				Score.builder()
					.user(gameEnded.getUser())
					.value(calculateHits(gameEnded))
					.build());
	}

	private BigDecimal calculateHits(Game gameEnded) {
		return new BigDecimal(gameEnded.getRounds().stream().filter(Round::getCorrect).count());
	}

	@Override
	public Question answer(String gameCode, Integer selectedPosition) throws BusinessException {
		Game game = gameRepository.findByCode(gameCode);
		
		Optional<Round> roundAnswer = 
				game.getRounds().stream().filter(r -> r.getCorrect() == null).findFirst();
		
		if(!roundAnswer.isPresent()) {
			throw new BusinessException("Falha ao tentar responder um quiz (já estava respondido).");			
		}
		roundAnswer.get().setCorrect(checkIfPositionCorrect(roundAnswer.get(), selectedPosition));
		gameRepository.save(game);
		
		return converterWithEvaluation(roundAnswer.get());
	}

	private Question converterWithEvaluation(Round roundAnswer) {
		Option firstOptionWithEvaluation = movieConverter.convertFromDto(roundAnswer.getFirst());
		firstOptionWithEvaluation.setEvaluation(roundAnswer.getFirst().getEvaluation());
		Option secondOptionWithEvaluation = movieConverter.convertFromDto(roundAnswer.getSecond());
		secondOptionWithEvaluation.setEvaluation(roundAnswer.getSecond().getEvaluation());
		
		Question question = roundConverter.convertFromDto(roundAnswer);
		question.setFirst(firstOptionWithEvaluation);
		question.setSecond(secondOptionWithEvaluation);
		
		return question;
	}
	
	private boolean checkIfPositionCorrect(Round round, Integer position) {
		Movie movieSelected = position == 1 ? round.getFirst() : round.getSecond();
	    return round.getFirst().getEvaluation().doubleValue() <= movieSelected.getEvaluation().doubleValue() 
	    			&& round.getSecond().getEvaluation().doubleValue() <= movieSelected.getEvaluation().doubleValue();
	}
	
	private boolean checkIfExistsRoundPendent(Game game) {
		return game.getRounds().stream().anyMatch(r -> r.getCorrect() == null);
	}
}