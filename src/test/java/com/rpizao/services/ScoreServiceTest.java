package com.rpizao.services;

import java.math.BigDecimal;
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

import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Score;
import br.com.rpizao.entities.User;
import br.com.rpizao.repositories.ScoreRepository;
import br.com.rpizao.repositories.UserRepository;
import br.com.rpizao.services.ScoreService;
import br.com.rpizao.utils.CryptoUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { MovieBattleJpaConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@ActiveProfiles("test")
public class ScoreServiceTest {
	
	private User userTest;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private ScoreRepository scoreRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void init() {
		String salt = CryptoUtils.generateSalt();
		 userTest = User.builder()
				.login("teste@mail.com")
				.salt(salt)
				.password(CryptoUtils.hash("12345", salt))
				.code(salt)
				.name("Teste")
				.build();
		 
		 userRepository.save(userTest);
	}
	
	@Test
	public void sucessoAoPublicarNovoScore() {
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(35))
				.build());
		
		Score score = scoreRepository.findByUser(userTest);
		
		Assert.assertNotNull(score);
		Assert.assertEquals(35, score.getValue().intValue());
	}
	
	@Test
	public void sucessoAoAtualizarScoreQuandoNumeroDeAcertosFoiSuperado() {
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(50.1))
				.build());
		
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(50.2))
				.build());
		
		Score score = scoreRepository.findByUser(userTest);
		
		Assert.assertNotNull(score);
		Assert.assertEquals(50.2, score.getValue().doubleValue(), 0);
	}
	
	@Test
	public void sucessoAoFinalizarJogoSemSuperarOScore() {
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(34))
				.build());
		
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(33))
				.build());
		
		Score score = scoreRepository.findByUser(userTest);
		
		Assert.assertNotNull(score);
		Assert.assertEquals(34, score.getValue().intValue());
	}
	
	@Test
	public void sucessoAoListarTodosOsScores() {
		scoreService.publish(
				Score.builder()
				.user(userTest)
				.value(BigDecimal.valueOf(35))
				.build());
		
		List<ScoreResult> scores = scoreService.list();
		
		Assert.assertEquals(Integer.valueOf(1), Integer.valueOf(scores.size()));
		Assert.assertEquals(userTest.getCode(), scores.get(0).getUserCode());
	}
	
}
