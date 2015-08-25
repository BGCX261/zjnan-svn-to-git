package com.zjnan.app.exception;

public class ServiceException extends AMPException {

	private static final long serialVersionUID = 4872117656104037525L;

	public ServiceException(String message, String errorCode) {
		super(message, errorCode);
	}
}
