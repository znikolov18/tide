package com.tide.interview.exception;

/**
 * Exception related to domain object non-existence.
 */
public class DomainObjectNotFoundException extends RuntimeException {

	private String message;
	
	public DomainObjectNotFoundException(String entityName, int id) {
		this.message = String.format("%s with id %d could not be found", entityName, id);
	}

	@Override
	public String getMessage() {
		return message;
	}

}
