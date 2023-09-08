package com.ad.motorsportfantasy.dtos;

import java.util.HashSet;
import java.util.Set;

import com.ad.motorsportfantasy.model.Driver;
import com.ad.motorsportfantasy.model.Series;

public class SeriesDTO {
	private String name;

	private Set<DriverDTO> drivers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DriverDTO> getDrivers() {
		return drivers;
	}

	public void setDrivers(Set<DriverDTO> drivers) {
		this.drivers = drivers;
	}

	public SeriesDTO() {
	}

	public SeriesDTO(String name, Set<DriverDTO> drivers) {
		this.name = name;
		this.drivers = drivers;
	}
	
	public static SeriesDTO fromSeries(Series series) {
		Set<DriverDTO> drivers = new HashSet<>();

		for (Driver d : series.getDrivers()) {
			drivers.add(DriverDTO.fromDriver(d));
		}

		return new SeriesDTO(series.getName(), drivers);
	}
}
