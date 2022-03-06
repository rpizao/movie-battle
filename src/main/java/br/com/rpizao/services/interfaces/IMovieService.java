package br.com.rpizao.services.interfaces;

import java.util.List;

import br.com.rpizao.entities.Movie;
import br.com.rpizao.exceptions.BusinessException;

public interface IMovieService {

	/**
	 * Busca um par de filmes na API do IMDb, sem ser repetido.
	 * 
	 * @return lista com dois filmes diferentes.
	 */
	List<Movie> loadingUniqueTupleOfMovies() throws BusinessException;
}
