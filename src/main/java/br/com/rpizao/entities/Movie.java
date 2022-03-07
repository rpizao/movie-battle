package br.com.rpizao.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.rpizao.utils.NumberUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "MOVIES")
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	@EqualsAndHashCode.Include
	private String imdbID;
	
	@Column
	@EqualsAndHashCode.Include
	private String title;
	
	@Column
	private String released;
	
	@Column
	private String runtime;
	
	@Column
	private String genre;
	
	@Column
	private String imdbRating;
	
	@Column
	private String imdbVotes;
	
	@Column
	public String plot;
	
	@Column
	public String poster;
	
	public BigDecimal getEvaluation() {
		if(!NumberUtils.isNumeric(imdbRating) || !NumberUtils.isNumeric(imdbVotes)) return BigDecimal.ZERO;
		
		final double rating = NumberUtils.toFloat(imdbRating);
		final int votes = NumberUtils.toInteger(imdbVotes);
		return BigDecimal.valueOf(rating * votes).setScale(2, RoundingMode.HALF_EVEN);
	}
}
