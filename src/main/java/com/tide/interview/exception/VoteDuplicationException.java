package com.tide.interview.exception;

/**
 * Exception related to vote duplication.
 */
public class VoteDuplicationException extends RuntimeException {

	private String message;

	public VoteDuplicationException(Integer announcementId, Integer userId) {
		this.message = String.format("vote for announcement with id %d by user with id %d already exists",
				announcementId, userId);
	}

	@Override
	public String getMessage() {
		return message;
	}
}
