package br.com.rpizao.services.interfaces;

import br.com.rpizao.entities.Score;

public interface IScoreService {
	
	/**
	 * Publica o score do usuário.
	 * 
	 * @param score
	 */
	public void publish(Score score);
}
