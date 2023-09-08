package com.ad.motorsportfantasy.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.motorsportfantasy.model.Series;
import com.ad.motorsportfantasy.repos.SeriesRepository;

@RestController
public class SeriesController {
	private SeriesRepository seriesRepository;

	SeriesController(SeriesRepository seriesRepository) {
		this.seriesRepository = seriesRepository;
	}

	@GetMapping("/series")
	public Set<String> getSeries() {
		Set<String> series = new HashSet<>();

		for(Series s : seriesRepository.findAll()) {
			series.add(s.getName());
		}

		return series;
	}
}
