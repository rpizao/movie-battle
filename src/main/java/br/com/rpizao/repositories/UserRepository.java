package br.com.rpizao.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rpizao.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByLogin(String login);
	User findByCode(String code);
}