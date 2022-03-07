package com.rpizao.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.rpizao.MovieBattleApplication;
import br.com.rpizao.config.WebConfig;
import br.com.rpizao.dtos.AuthUser;
import br.com.rpizao.dtos.UserCredentials;
import br.com.rpizao.services.interfaces.IUserService;


@SpringBootTest(classes = {MovieBattleApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class AuthControllerTest {

	@MockBean
    private IUserService userService;
    
    
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
	public void sucessoAoRealizarLogin() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    
	    UserCredentials credentials = UserCredentials.builder()
	    	.username("João")
	    	.password("tst01")
	    	.build();
	    
		when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(AuthUser.builder().build());
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(credentials))
				.headers(headers))
		.andExpect(status().isOk());
	}
	
	@Test
	public void fallhaAoRealizarLogin() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    
	    UserCredentials credentials = UserCredentials.builder()
	    	.username("João")
	    	.password("tst01")
	    	.build();
	    
		when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(credentials))
				.headers(headers))
		.andExpect(status().isForbidden());
	}
	
	@Test
	public void sucessoAoRealizarLogout() throws Exception {
		mockMvc.perform(get("/auth/logout")
				.headers(headers))
		.andExpect(status().isOk());
	}
	
}
