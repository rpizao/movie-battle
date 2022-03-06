package br.com.rpizao.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

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
}
