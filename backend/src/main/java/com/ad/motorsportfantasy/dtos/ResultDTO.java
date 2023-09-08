package com.ad.motorsportfantasy.dtos;

public class ResultDTO {
	private int points;

	private int index;

	private String name;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ResultDTO() {
	}

	public ResultDTO(int points, int index, String name) {
		this.points = points;
		this.index = index;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
