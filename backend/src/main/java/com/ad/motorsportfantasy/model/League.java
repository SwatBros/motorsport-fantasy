package com.ad.motorsportfantasy.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class League {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@ManyToOne()
	private User owner;

	@OneToMany(mappedBy = "league")
	Set<Player> participants;

	@ManyToMany
	Set<Series> series;
	
	public Set<Series> getSeries() {
		if (series == null) {
			series = new HashSet<>();
		}
		return series;
	}

	public void setSeries(Set<Series> series) {
		this.series = series;
	}

	public League() {
	}

	public League(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<Player> getParticipants() {
		if (participants == null) {
			participants = new HashSet<>();
		}
		return participants;
	}

	public void setParticipants(Set<Player> participants) {
		this.participants = participants;
	}

}