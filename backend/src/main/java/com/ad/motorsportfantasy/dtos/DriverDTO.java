package com.ad.motorsportfantasy.dtos;

import java.util.HashSet;
import java.util.Set;

import com.ad.motorsportfantasy.model.Driver;
import com.ad.motorsportfantasy.model.Result;

public class DriverDTO {
	private String name;

	private Integer points;

	private Float trend;

	private Integer position;

	private String imgRef;

	private Integer value;

	private Set<ResultDTO> results;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public DriverDTO() {
	}

	public DriverDTO(String name, Integer points, Float trend, Integer position, String imgRef, Integer value) {
		this.name = name;
		this.points = points;
		this.trend = trend;
		this.position = position;
		this.imgRef = imgRef;
		this.value = value;
	}

	public static DriverDTO fromDriver(Driver driver) {
		DriverDTO driverDTO = new DriverDTO(driver.getFullname(), driver.getPoints(), driver.getTrend(),
			driver.getPosition(), driver.getImgRef(), driver.getValue());
		
		for (Result result : driver.getResults()) {
			driverDTO.getResults().add(new ResultDTO(result.getPoints(), result.getIndex(), result.getName()));
		}

		return driverDTO;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Set<ResultDTO> getResults() {
		if (results == null) {
			results = new HashSet<>();
		}
		return results;
	}

	public void setResults(Set<ResultDTO> results) {
		this.results = results;
	}

	
}
