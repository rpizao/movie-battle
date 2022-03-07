package br.com.rpizao.services.interfaces;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.ScoreResult;
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
	 * Finaliza uma rodada selecionando entre as duas opções.
	 * 
	 * @param code
	 * @param selectedPosition
	 * 
	 * @return
	 * @throws BusinessException
	 */
	void answer(String code, Integer selectedPosition) throws BusinessException;
	
	/**
	 * Finaliza a batalha, armazena o resultado do usuário.
	 * 
	 * @param scoreResult
	 * @return
	 * @throws BusinessException
	 */
	void finish(ScoreResult scoreResult) throws BusinessException;
}
