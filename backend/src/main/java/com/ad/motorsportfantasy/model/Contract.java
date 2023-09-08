package com.ad.motorsportfantasy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contract {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Player contractor;

	@ManyToOne
	private Driver driver;

	private Integer value;

	private Integer points;

	private Integer startEvent;

	private Integer updatedPoints;

	private Float trend;
	
	public Float getTrend() {
		return trend;
	}

	public void setTrend(Float trend) {
		this.trend = trend;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getContractor() {
		return contractor;
	}

	public void setContractor(Player contractor) {
		this.contractor = contractor;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Contract() {
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getUpdatedPoints() {
		return updatedPoints;
	}

	public void setUpdatedPoints(Integer updatedPoints) {
		this.updatedPoints = updatedPoints;
	}

	public Integer getStartEvent() {
		return startEvent;
	}

	public void setStartEvent(Integer startEvent) {
		this.startEvent = startEvent;
	}

	public Contract(Player contractor, Driver driver, Integer startEvent, Integer value, Integer points,
			Integer updatedPoints, Float trend) {
		this.contractor = contractor;
		this.driver = driver;
		this.value = value;
		this.points = points;
		this.startEvent = startEvent;
		this.updatedPoints = updatedPoints;
		this.trend = trend;
	}
	
}
