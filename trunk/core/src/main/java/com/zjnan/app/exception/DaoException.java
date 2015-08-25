package com.zjnan.app.exception;

public class DaoException extends AMPException{

	private static final long serialVersionUID = 8037899278793078796L;

	public DaoException(String message, String errorCode) {
		super(message, errorCode);
	}

}
