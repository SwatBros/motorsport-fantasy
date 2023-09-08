package com.ad.motorsportfantasy.dtos;

import java.util.HashSet;
import java.util.Set;

import com.ad.motorsportfantasy.model.League;
import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.model.Series;

public class LeagueDTO {
	private String name;
	private String owner;
	private Set<PlayerDTO> players;
	private Set<SeriesDTO> series;

	public LeagueDTO() {
	}

	public LeagueDTO(String name, String owner, Set<PlayerDTO> players, Set<SeriesDTO> series) {
		this.name = name;
		this.owner = owner;
		this.players = players;
		this.series = series;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<PlayerDTO> getPlayers() {
		return players;
	}

	public void setPlayers(Set<PlayerDTO> players) {
		this.players = players;
	}

	public static LeagueDTO fromLeague(League league) {
		String owner = league.getOwner() == null ? "" : league.getOwner().getUsername();
	
		Set<PlayerDTO> players = new HashSet<>();
		for (Player participant : league.getParticipants()) {
			players.add(PlayerDTO.fromPlayer(participant));
		}
		
		Set<SeriesDTO> series = new HashSet<>();
		for (Series s : league.getSeries()) {
			series.add(SeriesDTO.fromSeries(s));
		}

		return new LeagueDTO(league.getName(), owner, players, series);
	}
	
	public Set<SeriesDTO> getSeries() {
		return series;
	}

	public void setSeries(Set<SeriesDTO> series) {
		this.series = series;
	}
	
}
