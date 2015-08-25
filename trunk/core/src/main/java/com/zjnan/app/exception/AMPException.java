package com.zjnan.app.exception;

import static com.zjnan.app.util.DateUtil.*;

import java.util.Date;

public class AMPException extends RuntimeException {

	private static final long serialVersionUID = -3888868544018808942L;

	public String errorCode;
	
	public AMPException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
		System.out.println(getDateTime(getDateTimePattern(), new Date()) + ":  (" + errorCode + ")  " + message);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
