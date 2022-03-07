package com.rpizao.services;

import java.math.BigDecimal;

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

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Game;
import br.com.rpizao.entities.User;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.repositories.GameRepository;
import br.com.rpizao.repositories.UserRepository;
import br.com.rpizao.services.GameService;
import br.com.rpizao.utils.CryptoUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { MovieBattleJpaConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@ActiveProfiles("test")
public class GameServiceTest {
	
	private String userCodeTest = "TST-CODE";
	private Battle battle;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Before
	public void init() {
		String salt = CryptoUtils.generateSalt();

		User userTest = User.builder()
			.login("teste@mail.com")
			.salt(salt)
			.password(CryptoUtils.hash("12345", salt))
			.code(userCodeTest)
			.name("Teste")
			.build();
		
		userRepository.save(userTest);
		
		battle = gameService.start(userCodeTest);
	}
	
	@Test
	public void sucessoAoIniciarUmNovoJogo() {
		Battle battle = gameService.start(userCodeTest);
		Assert.assertNotNull(battle);
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoTentarIniciarUmNovoJogoPassandoCodigoUsuarioDesconhecido() {
		gameService.start("01_CodigoIncorreto");
	}
	
	@Test
	public void sucessoAoSolicitarOProximoDesafioDentroDeUmJogoEmAndamento() {
		gameService.answer(battle.getGameCode(), 1); // Simulando uma resposta, selecionando primeiro filme.
		Battle battleUpdated = gameService.nextQuestion(battle.getGameCode());
		
		Assert.assertNotNull(battleUpdated);
		Assert.assertEquals(battle, battleUpdated);
		Assert.assertNotEquals(battleUpdated.getQuestion(), battle.getQuestion());
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoSolicitarOProximoDesafioDentroDeUmJogoEmAndamentoPassandoCodigoDesconhecido() {
		gameService.nextQuestion("Codigo01");
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoSolicitarOProximoDesafioTendoDesafioPendenteDeResposta() {
		Battle battleUpdated = gameService.nextQuestion(battle.getGameCode());
		
		Assert.assertNotNull(battleUpdated);
		Assert.assertEquals(battle, battleUpdated);
		Assert.assertNotEquals(battleUpdated.getQuestion(), battle.getQuestion());
	}
	
	@Test
	public void sucessoAoFinalizarUmJogo() {
		gameService.answer(battle.getGameCode(), 1); // Simulando uma resposta, selecionando primeiro filme.
		gameService.finish(ScoreResult.builder().gameCode(battle.getGameCode()).value(BigDecimal.valueOf(50)).build());
		
		Game game = gameRepository.findByCode(battle.getGameCode());
		Assert.assertNotNull(game.getFinish());
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoTentarFinalizarUmJogoPassandoCodigoDesconhecido() {
		gameService.finish(ScoreResult.builder().gameCode("Codigo01").value(BigDecimal.valueOf(50)).build());
		
		Game game = gameRepository.findByCode(battle.getGameCode());
		Assert.assertNotNull(game.getFinish());
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoFinalizarUmJogoQuePossuiRodadasNaoRespondidas() {
		gameService.finish(ScoreResult.builder().gameCode(battle.getGameCode()).value(BigDecimal.valueOf(50)).build());
		
		Game game = gameRepository.findByCode(battle.getGameCode());
		Assert.assertNotNull(game.getFinish());
	}
	
	@Test
	public void sucessoAoResponderUmQuizNoJogo() {
		Battle newBattle = gameService.start(userCodeTest);
		gameService.answer(newBattle.getGameCode(), 1);
		
		Game gameUpdated = gameRepository.findByCode(newBattle.getGameCode());
		
		Assert.assertNotNull(gameUpdated);
		Assert.assertTrue(gameUpdated.getRounds().stream().allMatch(r -> r.getCorrect() != null));
	}
	
	@Test(expected = BusinessException.class)
	public void falhaAoTentarResponderUmQuizJaRespondido() {
		Battle newBattle = gameService.start(userCodeTest);
		gameService.answer(newBattle.getGameCode(), 1);
		gameService.answer(newBattle.getGameCode(), 1);
	}
}
