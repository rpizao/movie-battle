package br.com.rpizao.converters;

import org.apache.commons.lang3.NotImplementedException;

import br.com.rpizao.dtos.Option;
import br.com.rpizao.entities.Movie;

public class MovieConverter extends Converter<Movie, Option> {

	public MovieConverter() {
		super(MovieConverter::convertToDto, MovieConverter::convertToEntity);
	}

	private static Option convertToDto(Movie movie) {
		return Option.builder()
				.name(movie.getTitle())
				.description(movie.getPlot())
				.evaluation(movie.getEvaluation())
				.picture(movie.getPoster())
				.build();
	}

	private static Movie convertToEntity(Option option) {
		throw new NotImplementedException("Não implementado");
	}
}
