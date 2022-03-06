package br.com.rpizao.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rpizao.entities.Score;
import br.com.rpizao.entities.User;

public interface ScoreRepository extends CrudRepository<Score, Long>{
	
	Score findByUser(User user);
	
}