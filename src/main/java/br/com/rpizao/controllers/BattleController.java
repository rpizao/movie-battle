package br.com.rpizao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Credentials;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.services.interfaces.IGameService;

@RestController
@RequestMapping(value = "/battle")
@CrossOrigin(origins = "http://localhost:4201")
public class BattleController {
	
	@Autowired
	private IGameService gameService;
	
	@PostMapping(value = "/start")
	public ResponseEntity<Battle> startBattle(@RequestBody Credentials credential) {
		try {
			Battle battle = gameService.start(credential.getCode());
			return new ResponseEntity<>(battle, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/next")
	public ResponseEntity<Battle> nextQuestion(@RequestBody Credentials credential) {
		try {
			Battle battle = gameService.nextQuestion(credential.getGameCode());
			return new ResponseEntity<>(battle, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/finish")
	public ResponseEntity<Boolean> finish(@RequestBody ScoreResult result) {
		try {
			gameService.finish(result.getGameCode(), result.getTotalHits());
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
