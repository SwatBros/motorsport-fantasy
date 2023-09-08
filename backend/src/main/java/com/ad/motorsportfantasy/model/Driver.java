package com.ad.motorsportfantasy.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Driver {
	@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fullname;

	private Integer points;

	private Float trend; 

	private Integer position;

	private String imgRef;

	private Integer value;

	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
	private Set<Result> results;

	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	@ManyToOne()
	private Series series;

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Driver() {
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Float getTrend() {
		return trend;
	}

	public void setTrend(Float trend) {
		this.trend = trend;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getImgRef() {
		return imgRef;
	}

	public void setImgRef(String imgRef) {
		this.imgRef = imgRef;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Set<Result> getResults() {
		if (results == null) {
			results = new HashSet<>();
		}
		return results;
	}

	public void setResults(Set<Result> results) {
		this.results = results;
	}

	
}