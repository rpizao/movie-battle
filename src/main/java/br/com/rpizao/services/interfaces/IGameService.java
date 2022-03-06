package br.com.rpizao.services.interfaces;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.exceptions.BusinessException;

public interface IGameService {
	
	/**
	 * Cria uma nova partida (batalha) para o usuário logado.
	 * 
	 * @return o primeiro quiz para a partida.
	 */
	Battle start(String userCode) throws BusinessException;
	
	/**
	 * Carrega os próximos dois filmes para a batalha.
	 * 
	 * @return
	 * @throws BusinessException
	 */
	Battle nextQuestion(String code) throws BusinessException;
	
	/**
	 * Finaliza a batalha, armazena o resultado do usuário.
	 * 
	 * @param code código do jogo
	 * @param totalHits total de acertos do usuário
	 * @return
	 * @throws BusinessException
	 */
	void finish(String code, Long totalHits) throws BusinessException;
}
