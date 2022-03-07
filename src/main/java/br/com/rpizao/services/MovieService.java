package br.com.rpizao.services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.rpizao.entities.Movie;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.repositories.MovieRepository;
import br.com.rpizao.services.interfaces.IMovieService;
import br.com.rpizao.utils.NumberUtils;
import br.com.rpizao.utils.RandomListUtils;

@Service
public class MovieService implements IMovieService {
	
	@Autowired
	private MovieRepository movieRepository;
	
	private ObjectMapper mapper;
	
	@Value("${user.api.imdb.search_by_title}")
	private String apiImdbSearchByTitle;
	
	@Value("${user.api.imdb.search_by_id}")
	private String apiImdbSearchById;
	
	
	@PostConstruct
	private void init() {
		mapper = new ObjectMapper();
	    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
	    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private Movie findEveryoneMovieInApi() throws BusinessException {
		final String term = RandomListUtils.randomWord();
	    
	    try {
	    	final String listMoviesContent = searchInExternalApi(apiImdbSearchByTitle + term);
	    	final ObjectNode node = mapper.readValue(listMoviesContent, ObjectNode.class);
	    	if(Boolean.FALSE.equals(Boolean.valueOf(node.get("Response").textValue()))) {
	    		return findEveryoneMovieInApi();
	    	}
	    	
	    	final List<Movie> movies = Arrays.asList(mapper.readValue(node.get("Search").toString(), Movie[].class));
	    	Movie movieSelected = RandomListUtils.randomOnList(movies);
	    	
	    	final String movieSelectedContent = searchInExternalApi(apiImdbSearchById + movieSelected.getImdbID());
			movieSelected = mapper.readValue(movieSelectedContent, Movie.class);
	    	
			if(!movieIsValid(movieSelected)) {
				return findEveryoneMovieInApi();
			}
			return movieSelected;
	    	
		} catch (Exception e) {
			throw new BusinessException("Erro ao converter json para Movie class.", e);
		}
	}
	
	private String searchInExternalApi(String uri) {
		RestTemplate restTemplate = new RestTemplate();
	    return restTemplate.getForObject(uri, String.class);
	}
	
	@Override
	public List<Movie> loadingUniqueTupleOfMovies() throws BusinessException {
		Movie first = findEveryoneMovieInApi();
		
		Movie second = findEveryoneMovieInApi();
		while(first.equals(second)) {
			second = findEveryoneMovieInApi();	
		}
		
		List<Movie> tuple = Arrays.asList(first, second);
		movieRepository.saveAll(tuple);	
		return tuple;
	}
	
	private boolean movieIsValid(Movie movie) {
		return movie != null 
				&& NumberUtils.isNumeric(movie.getImdbRating()) 
					&& NumberUtils.isNumeric(movie.getImdbVotes())
						&& !StringUtils.isEmpty(movie.getTitle());
	}
	
}