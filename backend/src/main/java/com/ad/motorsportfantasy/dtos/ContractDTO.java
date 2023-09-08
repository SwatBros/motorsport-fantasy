package com.ad.motorsportfantasy.dtos;

import com.ad.motorsportfantasy.model.Contract;
import com.ad.motorsportfantasy.model.Result;

public class ContractDTO {
	private String driverName;

	private Integer value;

	private Integer points;

	private Float trend;

	private String start;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public ContractDTO() {
	}

	public ContractDTO(String driverName, Integer value, Integer points, Float trend, String start) {
		this.driverName = driverName;
		this.value = value;
		this.points = points;
		this.trend = trend;
		this.start = start;
	}

	public static ContractDTO fromContract(Contract contract) {
		return new ContractDTO(contract.getDriver().getFullname(), contract.getValue(),
			contract.getUpdatedPoints() - contract.getPoints(), contract.getTrend(),
			contract.getDriver().getResults().stream().filter(r -> r.getIndex() == contract.getStartEvent())
				.findFirst().orElse(new Result(0, 0, "Event")).getName());
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
	
}
