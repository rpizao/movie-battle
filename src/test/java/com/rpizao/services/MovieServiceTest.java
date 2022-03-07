package com.rpizao.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.rpizao.config.MovieBattleJpaConfig;

import br.com.rpizao.entities.Movie;
import br.com.rpizao.services.MovieService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { MovieBattleJpaConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@ActiveProfiles("test")
public class MovieServiceTest {
	
	@Autowired
	private MovieService movieService;
	
	@Before
	public void init() { }
	
	@Test
	public void carregarViaApiDoisFilmesNaoRepetidosEValidos() {
		List<Movie> movies = movieService.loadingUniqueTupleOfMovies();
		Assert.assertNotNull(movies);
		Assert.assertEquals(2, movies.size());
		Assert.assertNotEquals(movies.get(0), movies.get(1));
	}
	
}
