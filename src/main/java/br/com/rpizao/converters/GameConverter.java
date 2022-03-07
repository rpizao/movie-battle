package br.com.rpizao.converters;


import org.apache.commons.lang3.NotImplementedException;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Question;
import br.com.rpizao.entities.Game;
import br.com.rpizao.entities.Round;

public class GameConverter extends Converter<Game, Battle> {
	
	private static final MovieConverter movieConverter = new MovieConverter();

	public GameConverter() {
		super(GameConverter::convertToDto, GameConverter::convertToEntity);
	}

	private static Battle convertToDto(Game game) {
		Round lastRound = game.getRounds().get(game.getRounds().size() - 1);
		
		Question lastQuestion = Question.builder()
				.sequence(lastRound.getSequence())
				.first(movieConverter.convertFromDto(lastRound.getFirst()))
				.second(movieConverter.convertFromDto(lastRound.getSecond()))
				.build();
		
		return Battle.builder()
					.gameCode(game.getCode())
					.question(lastQuestion)
					.build();
	}

	private static Game convertToEntity(Battle battle) {
		throw new NotImplementedException("Não implementado");
	}
}
