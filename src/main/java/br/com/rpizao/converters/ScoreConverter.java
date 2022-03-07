package br.com.rpizao.converters;


import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Score;
import br.com.rpizao.entities.User;

public class ScoreConverter extends Converter<Score, ScoreResult> {

	public ScoreConverter() {
		super(ScoreConverter::convertToDto, ScoreConverter::convertToEntity);
	}

	private static ScoreResult convertToDto(Score score) {
		return ScoreResult.builder()
				.userCode(score.getUser().getCode())
				.userName(score.getUser().getName())
				.value(score.getValue())
				.build();
	}

	private static Score convertToEntity(ScoreResult scoreResult) {
		return Score.builder()
				.user(User.builder().code(scoreResult.getUserCode()).build())
				.value(scoreResult.getValue())
				.build();
	}
}
