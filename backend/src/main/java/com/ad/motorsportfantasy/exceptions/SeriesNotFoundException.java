package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Series not found")
public class SeriesNotFoundException extends Exception {
	public SeriesNotFoundException(String msg) {
		super(msg);
	}	
}
