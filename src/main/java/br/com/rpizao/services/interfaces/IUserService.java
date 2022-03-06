package br.com.rpizao.services.interfaces;

import br.com.rpizao.dtos.AuthUser;
import br.com.rpizao.entities.User;
import br.com.rpizao.exceptions.BusinessException;

public interface IUserService {
	
	/**
	 * Login utilizando password criptografado com a técnica Pepper and Salt.
	 * 
	 * @param login
	 * @param passw
	 * @return objeto contendo as informações necessárias e seguras 
	 * para identificar o usuário logado.
	 */
	AuthUser login(String login, String passw) throws BusinessException;
	
	/**
	 * Busca o usuário pelo código.
	 * 
	 * @param code
	 * @return
	 */
	public User findByCode(String code);
}
