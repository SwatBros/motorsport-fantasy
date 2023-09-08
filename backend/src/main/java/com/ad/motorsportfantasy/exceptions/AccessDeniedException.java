package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Access Denied")
public class AccessDeniedException extends Exception {
	public AccessDeniedException(String msg) {
		super(msg);
	}
}
