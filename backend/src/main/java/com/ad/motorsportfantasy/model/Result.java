package com.ad.motorsportfantasy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Result {
	@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer points;

	private Integer index;

	private String name;

	@ManyToOne()
	private Driver driver;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Result(Integer points, Integer index, String name) {
		this.points = points;
		this.index = index;
		this.name = name;
	}

	public Result() {
	}
	
}
