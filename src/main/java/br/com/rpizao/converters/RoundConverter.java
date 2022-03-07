package br.com.rpizao.converters;


import br.com.rpizao.dtos.Question;
import br.com.rpizao.entities.Round;

public class RoundConverter extends Converter<Round, Question> {
	
	private static final MovieConverter movieConverter = new MovieConverter();

	public RoundConverter() {
		super(RoundConverter::convertToDto, RoundConverter::convertToEntity);
	}

	private static Question convertToDto(Round round) {
		return Question.builder()
				.first(movieConverter.convertFromDto(round.getFirst()))
				.second(movieConverter.convertFromDto(round.getSecond()))
				.correct(round.getCorrect())
				.build();
	}

	private static Round convertToEntity(Question question) {
		return Round.builder()
			.first(movieConverter.convertFromEntity(question.getFirst()))
			.second(movieConverter.convertFromEntity(question.getSecond()))
			.build();
	}
}
