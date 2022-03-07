package com.rpizao.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.rpizao.MovieBattleApplication;
import br.com.rpizao.config.WebConfig;
import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Option;
import br.com.rpizao.dtos.Question;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.services.interfaces.IGameService;
import br.com.rpizao.services.interfaces.IScoreService;


@SpringBootTest(classes = {MovieBattleApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class BattleControllerTest {

	@MockBean
    private IGameService gameService;
    
    @MockBean
    private IScoreService scoreService;
    
    
    private static HttpHeaders headers = new HttpHeaders();
    
    static {
    	headers.add("Access-Control-Allow-Origin", "*");
    	headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
    	headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token");
    }
    
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
	@Test
	public void codigo200AoIniciarNovoJogo() throws Exception {
		Battle battle = Battle.builder()
				.gameCode("G01")
				.question(Question.builder().build())
				.build();
	
		when(gameService.start(Mockito.anyString())).thenReturn(battle);
		
		mockMvc.perform(get("/battle/xpto/start")
				.headers(headers))
		.andExpect(status().isOk());
	}
	
	@Test
	public void codigo400AoIniciarNovoJogoComCodigoInvalido() throws Exception {
		when(gameService.start(Mockito.anyString())).thenThrow(BusinessException.class);
		
		mockMvc.perform(get("/battle/XPTO/start")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void codigo200AoSolicitarNovaQuestao() throws Exception {
		Battle battle = Battle.builder()
				.gameCode("G01")
				.question(Question.builder().build())
				.build();
		
		when(gameService.nextQuestion(Mockito.anyString())).thenReturn(battle);
		
		mockMvc.perform(get("/battle/G01/next")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void codigo400AoSolicitarNovaQuestao() throws Exception {
		when(gameService.nextQuestion(Mockito.anyString())).thenThrow(BusinessException.class);
		
		mockMvc.perform(get("/battle/G01/next")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void codigo200AoResponderUmQuiz() throws Exception {
		Question question = Question.builder()
				.first(Option.builder().build())
				.second(Option.builder().build())
				.build();
		
		when(gameService.answer(Mockito.anyString(), Mockito.anyInt())).thenReturn(question);
		
		mockMvc.perform(put("/battle/G01/answer/1")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void codigo400AoResponderUmQuiz() throws Exception {
		when(gameService.answer(Mockito.anyString(), Mockito.anyInt())).thenThrow(BusinessException.class);
		
		mockMvc.perform(put("/battle/G01/answer/1")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isBadRequest());
	}
	
	@Test
	public void codigo200AoFinalizarUmJogo() throws Exception {
		doNothing().when(gameService).finish(Mockito.anyString());
		
		mockMvc.perform(put("/battle/G01/finish")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void codigo400AoFinalizarUmJogo() throws Exception {
		doThrow(BusinessException.class).when(gameService).finish(Mockito.anyString());
		
		mockMvc.perform(put("/battle/G01/finish")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isBadRequest());
	}

	@Test
	public void codigo200AoListarTodosOsScores() throws Exception {
		when(scoreService.list()).thenReturn(Arrays.asList(ScoreResult.builder().build()));
		
		mockMvc.perform(get("/battle/scores")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	public void codigo400AoTentarListarTodosOsScores() throws Exception {
		when(scoreService.list()).thenThrow(BusinessException.class);
		
		mockMvc.perform(get("/battle/scores")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isBadRequest());
	}
	
}
