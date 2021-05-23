package com.gmail.edugsdf.exceptions;

public class ProductNotFundException extends RuntimeException {

	private static final long serialVersionUID = 6891524270580182249L;

	public ProductNotFundException(String msg) {
		super(msg);
	}
}
