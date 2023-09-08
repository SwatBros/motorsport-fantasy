package com.ad.motorsportfantasy.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Series {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String apiRef;

	@Column(unique = true)
	private String name;

	@ManyToMany(mappedBy = "series")
	private Set<League> leagues;

	@OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
	private Set<Driver> drivers;

	@OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
	private Set<Standings> standings;

	public Set<Driver> getDrivers() {
		if (drivers == null) {
			drivers = new HashSet<>();
		}
		return drivers;
	}

	public void setDrivers(Set<Driver> drivers) {
		this.drivers = drivers;
	}

	public String getApiRef() {
		return apiRef;
	}

	public void setApiRef(String apiRef) {
		this.apiRef = apiRef;
	}

	public Set<League> getLeagues() {
		if (leagues == null) {
			leagues = new HashSet<>();
		}
		return leagues;
	}

	public void setLeagues(Set<League> leagues) {
		this.leagues = leagues;
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

	public Series() {
	}

	public Series(String name) {
		this.name = name;
	}

	public Set<Standings> getStandings() {
		return standings;
	}

	public void setStandings(Set<Standings> standings) {
		this.standings = standings;
	}

	
}
