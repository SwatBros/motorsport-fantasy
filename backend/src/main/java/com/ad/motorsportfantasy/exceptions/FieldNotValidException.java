package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Field not valid")
public class FieldNotValidException extends Exception {
	public FieldNotValidException(String msg) {
		super(msg);
	}
}
