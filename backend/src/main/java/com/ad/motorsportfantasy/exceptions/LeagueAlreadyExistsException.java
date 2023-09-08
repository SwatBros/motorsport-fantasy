package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "League already exists")
public class LeagueAlreadyExistsException extends Exception {
	public LeagueAlreadyExistsException(String msg) {
		super(msg);
	}
}
