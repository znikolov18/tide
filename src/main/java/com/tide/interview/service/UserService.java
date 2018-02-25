package com.tide.interview.service;

import com.tide.interview.dto.UserDto;
import com.tide.interview.exception.DomainObjectNotFoundException;

/**
 * Service contains method related to user business logic.
 */
public interface UserService {

	/**
	 * Gets user.
	 * 
	 * @param userId user id.
	 * @return user data transfer object.
	 */
	UserDto getUserDto(int userId) throws DomainObjectNotFoundException ;

	/**
	 * Creates user.
	 * 
	 * @param userDto user data transfer object
	 * @return id of created user
	 */
	Integer createUser(UserDto userDto);

	/**
	 * Checks user existence.
	 * 
	 * @param userId user id
	 * @return true if exists, otherwise false
	 */
	Boolean checkIfUserExists(int userId);

}
