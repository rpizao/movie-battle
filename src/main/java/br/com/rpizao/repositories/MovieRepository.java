package br.com.rpizao.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rpizao.entities.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long>{ }