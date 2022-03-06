package br.com.rpizao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rpizao.dtos.AuthUser;
import br.com.rpizao.dtos.Credentials;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.services.interfaces.IUserService;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping(value = "login")
	public ResponseEntity<AuthUser> login(@RequestBody Credentials credentials) throws BusinessException {
		final AuthUser authUserIfAccept = 
				userService.login(credentials.getUsername(), credentials.getPassword());
		return new ResponseEntity<>(authUserIfAccept, HttpStatus.OK);
	}
	
	@GetMapping(value = "logout")
	public ResponseEntity<Boolean> logout() {
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
	
}
