package br.com.rpizao.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rpizao.converters.ScoreConverter;
import br.com.rpizao.dtos.ScoreResult;
import br.com.rpizao.entities.Score;
import br.com.rpizao.repositories.ScoreRepository;
import br.com.rpizao.services.interfaces.IScoreService;

@Service
public class ScoreService implements IScoreService {
	
	@Autowired
	private ScoreRepository scoreRepository;
	
	private ScoreConverter scoreConverter = new ScoreConverter();
	

	@Override
	public void publish(Score score) {
		Score lastScore = lastScore(score);
		if(lastScore == null) {
			scoreRepository.save(score);
		}
		else if(lastScore.getTotalHits() < score.getTotalHits()) {
			lastScore.setTotalHits(score.getTotalHits());
			scoreRepository.save(lastScore);
		}
	}
	
	private Score lastScore(Score score) {
		return scoreRepository.findByUser(score.getUser());
	}

	@Override
	public List<ScoreResult> list() {
		return StreamSupport
			.stream(scoreRepository.findAll().spliterator(), false)
			.map(score -> scoreConverter.convertFromDto(score))
			.sorted(Comparator.comparingLong(ScoreResult::getTotalHits).reversed())
			.collect(Collectors.toList());
	}
	
}