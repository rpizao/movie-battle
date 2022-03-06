package br.com.rpizao.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Option;
import br.com.rpizao.dtos.Question;
import br.com.rpizao.entities.Game;
import br.com.rpizao.entities.Movie;
import br.com.rpizao.entities.Round;
import br.com.rpizao.entities.Score;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.repositories.GameRepository;
import br.com.rpizao.services.interfaces.IGameService;
import br.com.rpizao.services.interfaces.IMovieService;
import br.com.rpizao.services.interfaces.IScoreService;
import br.com.rpizao.services.interfaces.IUserService;
import br.com.rpizao.utils.CryptoUtils;
import br.com.rpizao.utils.NumberUtils;

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
	

	@Override
	public Battle start(String userCode) throws BusinessException {
		Game game = Game.builder()
			.code(CryptoUtils.generateSalt())
			.create(LocalDateTime.now())
			.rounds(Arrays.asList(createRoundFirst()))
			.user(userService.findByCode(userCode)) 
			.build();
			
		gameRepository.save(game);
		return converterToBattle(game);
	}
	
	@Override
	public Battle nextQuestion(String code) throws BusinessException {
		Game game = gameRepository.findByCode(code);
		game.getRounds().add(createRound(game));
		return converterToBattle(game);
	}
	
	private Round createRoundFirst() throws BusinessException {
		return createRound(null);
	}
	
	private Round createRound(Game game) throws BusinessException {
		List<Movie> movies = loadingMoviesForRound(game);
		
		return Round.builder()
				.sequence(0)
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
	
	private Battle converterToBattle(Game game) {
		Round lastRound = game.getRounds().get(game.getRounds().size() - 1);
		
		Question lastQuestion = Question.builder()
				.first(converterToOption(lastRound.getFirst()))
				.second(converterToOption(lastRound.getSecond()))
				.build();
		
		return Battle.builder()
					.gameCode(game.getCode())
					.question(lastQuestion)
					.build();
	}

	private Option converterToOption(Movie movie) {
		final double rating = NumberUtils.toFloat(movie.getImdbRating());
		final int votes = NumberUtils.toInteger(movie.getImdbVotes());
		
		return Option.builder()
				.name(movie.getTitle())
				.description(movie.getPlot())
				.evaluation(BigDecimal.valueOf(rating * votes).setScale(2, RoundingMode.HALF_EVEN))
				.picture(movie.getPoster())
				.build();
	}

	@Override
	public void finish(String code, Long totalHits) throws BusinessException {
		Game gameEnded = gameRepository.findByCode(code);
		gameEnded.setFinish(LocalDateTime.now());
		gameRepository.save(gameEnded);
		
		scoreService.publish(
				Score.builder()
					.user(gameEnded.getUser())
					.totalHits(totalHits)
					.build());
	}
}