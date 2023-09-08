package com.ad.motorsportfantasy.dtos;

import com.ad.motorsportfantasy.model.Standings;

public class StandingsDTO {
	private String series;

	private String player;

	private Integer points;

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public StandingsDTO() {
	}

	public StandingsDTO(String series, String player, Integer points) {
		this.series = series;
		this.player = player;
		this.points = points;
	}

	public static StandingsDTO fromStandings(Standings s) {
		return new StandingsDTO(s.getSeries().getName(), s.getPlayer().getUser().getUsername(), s.getPoints());
	}
}
