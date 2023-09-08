package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "League not found")
public class LeagueNotFoundException extends Exception {
	public LeagueNotFoundException(String msg) {
		super(msg);
	}
}
