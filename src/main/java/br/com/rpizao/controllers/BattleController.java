package br.com.rpizao.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Question;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.exceptions.BusinessException;
import br.com.rpizao.services.interfaces.IGameService;
import br.com.rpizao.services.interfaces.IScoreService;

@RestController
@RequestMapping(value = "/battle")
@CrossOrigin(origins = "*")
public class BattleController {
	
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IScoreService scoreService;
	
	
	@GetMapping(value = "/{userCode}/start")
	public ResponseEntity<Battle> startBattle(@PathVariable(value = "userCode") String userCode) {
		try {
			Battle battle = gameService.start(userCode);
			return new ResponseEntity<>(battle, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{code}/next")
	public ResponseEntity<Battle> nextQuestion(@PathVariable(name = "code") String gameCode) {
		try {
			Battle battle = gameService.nextQuestion(gameCode);
			return new ResponseEntity<>(battle, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/{code}/answer/{position}")
	public ResponseEntity<Question> awnser(@PathVariable(name = "code") String gameCode, @PathVariable(name = "position") Integer position) {
		try {
			Question questionUpdated = gameService.answer(gameCode, position);
			return new ResponseEntity<>(questionUpdated, HttpStatus.OK);
			
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = "/{code}/finish")
	public ResponseEntity<Boolean> finish(@PathVariable(name = "code") String gameCode) {
		try {
			gameService.finish(gameCode);
			return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/scores")
	public ResponseEntity<List<ScoreResult>> getScores() {
		try {
			final List<ScoreResult> scores = scoreService.list();
			return new ResponseEntity<>(scores, HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
