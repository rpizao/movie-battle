package com.rpizao.services;

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

import br.com.rpizao.dtos.AuthUser;
import br.com.rpizao.entities.User;
import br.com.rpizao.repositories.UserRepository;
import br.com.rpizao.services.UserService;
import br.com.rpizao.utils.CryptoUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { MovieBattleJpaConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void init() {
		String salt = CryptoUtils.generateSalt();
		 
		User userTest = User.builder()
				.login("teste@mail.com")
				.salt(salt)
				.password(CryptoUtils.hash("12345", salt))
				.code(salt)
				.name("Teste")
				.build();
		 
		 userRepository.save(userTest);
	}
	
	@Test
	public void sucessoAoRealizarLogin() {
		AuthUser usuarioLogado = 
				userService.login("teste@mail.com", "12345");
		
		Assert.assertNotNull(usuarioLogado);
		Assert.assertNotNull(usuarioLogado.getToken());
		Assert.assertNotNull(usuarioLogado.getUser());
		Assert.assertEquals("Teste", usuarioLogado.getUser().getUsername());
	}
	
	@Test
	public void falhaAoRealizarLogin() {
		AuthUser usuarioLogado = 
				userService.login("teste@mail.com", "12345_");
		
		Assert.assertNull(usuarioLogado);
	}

}
