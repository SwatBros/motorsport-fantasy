package com.ad.motorsportfantasy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Contract already exists")
public class ContractAlreadyExistsException extends Exception {
	public ContractAlreadyExistsException(String msg) {
		super(msg);
	}
}
