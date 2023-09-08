package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Not enough money")
public class NotEnoughMoneyException extends Exception {
	public NotEnoughMoneyException(String msg) {
		super(msg);
	}
}
