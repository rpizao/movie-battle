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
			.rounds(Arrays.asList(createRound()))
			.user(userService.findByCode(userCode)) 
			.build();
			
		gameRepository.save(game);
		return converterToBattle(game);
	}
	
	@Override
	public Battle nextQuestion(String code) throws BusinessException {
		Game game = gameRepository.findByCode(code);
		game.getRounds().add(createRound());
		return converterToBattle(game);
	}
	
	private Round createRound() throws BusinessException {
		List<Movie> movies = movieService.loadingUniqueTupleOfMovies();
		return Round.builder()
				.sequence(0)
				.first(movies.get(0))
				.second(movies.get(1))
				.build();
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