package com.crimsonlogic.onlinebiddingsystem.exception;

public class UserAlreadyRegisteredException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4676954553086672579L;

	public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
