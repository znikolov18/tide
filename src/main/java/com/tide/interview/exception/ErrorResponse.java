package com.tide.interview.exception;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * List of errors, used for response body in case of unsuccessful requests.
 */
public class ErrorResponse {

	private List<String> errors = Lists.newArrayList();
	
	public ErrorResponse() {
	}

	public ErrorResponse(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
