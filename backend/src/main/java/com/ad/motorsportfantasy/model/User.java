package com.ad.motorsportfantasy.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@OneToMany(mappedBy = "owner")
	private Set<League> owned_leagues;

	@OneToMany(mappedBy = "user")
	private Set<Player> players;

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<League> getOwned_leagues() {
		if (owned_leagues == null) {
			owned_leagues = new HashSet<>();
		}
		return owned_leagues;
	}

	public void setOwned_leagues(Set<League> owned_leagues) {
		this.owned_leagues = owned_leagues;
	}

	public Set<Player> getPlayers() {
		if (players == null) {
			players = new HashSet<>();
		}
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
}