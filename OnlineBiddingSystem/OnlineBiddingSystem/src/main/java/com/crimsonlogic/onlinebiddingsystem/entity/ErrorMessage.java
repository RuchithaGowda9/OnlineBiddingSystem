package com.crimsonlogic.onlinebiddingsystem.entity;

import java.time.LocalDateTime;

/**
 * @author Ruchitha
 *
 */
public class ErrorMessage {
	private int statusCode;
	private LocalDateTime errorTimestamp;
	private String message;
	private String description;

	public ErrorMessage(int statusCode, LocalDateTime errorTimestamp, String message, String description) {
		super();
		this.statusCode = statusCode;
		this.errorTimestamp = errorTimestamp;
		this.message = message;
		this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public LocalDateTime getErrorTimestamp() {
		return errorTimestamp;
	}

	public void setErrorTimestamp(LocalDateTime errorTimestamp) {
		this.errorTimestamp = errorTimestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
