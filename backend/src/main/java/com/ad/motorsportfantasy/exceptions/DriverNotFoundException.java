package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Driver not found")
public class DriverNotFoundException extends Exception {
	public DriverNotFoundException(String msg) {
		super(msg);
	}	
}
