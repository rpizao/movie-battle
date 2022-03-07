package br.com.rpizao.services.interfaces;

import br.com.rpizao.dtos.Battle;
import br.com.rpizao.dtos.Question;
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
	 * @param code código do jogo
	 * @param selectedPosition 1, caso tenha escolhido a primeira opção, 2 para segunda.
	 * 
	 * @return a questão (DTO da entidade Round) indicando o acerto e os valores de cada opção (filme).
	 * @throws BusinessException
	 */
	Question answer(String code, Integer selectedPosition) throws BusinessException;
	
	/**
	 * Finaliza a batalha, armazena o resultado do usuário.
	 * 
	 * @param scoreResult
	 * @return
	 * @throws BusinessException
	 */
	void finish(ScoreResult scoreResult) throws BusinessException;
}
