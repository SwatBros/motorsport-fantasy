package com.ad.motorsportfantasy.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Player {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne()
	private User user;

	@ManyToOne()
	private League league;

	@Column(nullable = false)
	private Integer points;

	private Integer money;

	@OneToMany(mappedBy = "contractor")
	private Set<Contract> contracts;

	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	private Set<Standings> standings;

	public Player() {
	}
	
	public Player(User user, League league) {
		this.user = user;
		this.league = league;
		this.points = 0;
		this.money = 40000000;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Set<Contract> getContracts() {
		if (contracts == null) {
			contracts = new HashSet<>();
		}
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Set<Standings> getStandings() {
		if (standings == null) {
			standings = new HashSet<>();
		}
		return standings;
	}

	public void setStandings(Set<Standings> standings) {
		this.standings = standings;
	}

	
}
