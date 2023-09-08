package com.ad.motorsportfantasy.dtos;

import java.util.HashSet;
import java.util.Set;

import com.ad.motorsportfantasy.model.Contract;
import com.ad.motorsportfantasy.model.Player;

public class PlayerDTO {
	private String name;

	private Integer points;

	private Integer money;

	private Set<ContractDTO> contracts;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerDTO() {
	}

	public PlayerDTO(String name, Integer points, Integer money) {
		this.name = name;
		this.points = points;
		this.money = money;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public static PlayerDTO fromPlayer(Player player) {
		PlayerDTO p = new PlayerDTO(player.getUser().getUsername(), player.getPoints(), player.getMoney());
		for (Contract c : player.getContracts()) {
			p.getContracts().add(ContractDTO.fromContract(c));
		}

		return p;
	}

	public Set<ContractDTO> getContracts() {
		if (contracts == null) {
			contracts = new HashSet<>();
		}
		return contracts;
	}

	public void setContracts(Set<ContractDTO> contracts) {
		this.contracts = contracts;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
}
