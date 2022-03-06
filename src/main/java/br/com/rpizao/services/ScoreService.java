package br.com.rpizao.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rpizao.entities.Score;
import br.com.rpizao.repositories.ScoreRepository;
import br.com.rpizao.services.interfaces.IScoreService;

@Service
public class ScoreService implements IScoreService {
	
	@Autowired
	private ScoreRepository scoreRepository;

	@Override
	public void publish(Score score) {
		if(hasNewRecord(score)) {
			scoreRepository.save(score);
		}
	}
	
	private boolean hasNewRecord(Score score) {
		final Score actualScore = scoreRepository.findByUser(score.getUser());
		return actualScore.getTotalHits() < score.getTotalHits();
	}
	
}