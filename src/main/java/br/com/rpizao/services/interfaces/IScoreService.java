package br.com.rpizao.services.interfaces;

import java.util.List;

import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Score;

public interface IScoreService {
	
	/**
	 * Publica o score do usuário.
	 * 
	 * @param score
	 */
	public void publish(Score score);
	
	/**
	 * Lista os scores de todos os usuários.
	 * 
	 * @return lista dos scores.
	 */
	public List<ScoreResult> list();
}
