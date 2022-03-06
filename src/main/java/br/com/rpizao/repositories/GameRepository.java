package br.com.rpizao.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rpizao.entities.Game;

public interface GameRepository extends CrudRepository<Game, Long>{
	Game findByCode(String code);
}