package br.com.rpizao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROUNDS")
public class Round {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Column
	private Integer sequence;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "first_movie_id")
	private Movie first;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "second_movie_id")
	private Movie second;
	
}
