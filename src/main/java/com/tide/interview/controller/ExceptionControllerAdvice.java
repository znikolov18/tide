package com.tide.interview.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.common.collect.Lists;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.exception.ErrorResponse;
import com.tide.interview.exception.VoteDuplicationException;

/**
 * Exception handler controller advice
 */
@RestControllerAdvice
public class ExceptionControllerAdvice extends  ResponseEntityExceptionHandler {
	
	/**
	 * Handles exceptions related to domain object non-existence.
	 * 
	 * @param exception domain object not found exception
	 * @return response with status 404 NOT FOUND
	 */
	@ExceptionHandler(DomainObjectNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleDomainObjectNotFoundException(DomainObjectNotFoundException exception) {
		ErrorResponse errorResponse = new ErrorResponse(Lists.newArrayList(exception.getMessage()));		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handles exceptions related to vote duplication.
	 * 
	 * @param exception vote duplication exception
	 * @return response with status 409 CONFLICT
	 */
	@ExceptionHandler(VoteDuplicationException.class)
	public ResponseEntity<ErrorResponse> handleVoteDuplicationdException(VoteDuplicationException exception) {
		ErrorResponse errorResponse = new ErrorResponse(Lists.newArrayList(exception.getMessage()));		
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = Lists.newArrayList();
		exception.getBindingResult().getFieldErrors()
				.forEach(error -> errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage())));
		exception.getBindingResult().getGlobalErrors()
				.forEach(error -> errors.add(String.format("%s: %s", error.getObjectName(), error.getDefaultMessage())));

		ErrorResponse apiError = new ErrorResponse(errors);
		return handleExceptionInternal(exception, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}
	
}
