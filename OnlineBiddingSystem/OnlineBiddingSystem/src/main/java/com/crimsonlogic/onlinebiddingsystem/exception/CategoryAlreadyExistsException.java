package com.crimsonlogic.onlinebiddingsystem.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5354448150945308510L;

	public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
