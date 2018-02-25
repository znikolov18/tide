package com.tide.interview.persistence.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.Repository;

import com.tide.interview.persistence.domain.User;

/**
 * Repository for user related operations.
 */
public interface UserRepository extends Repository<User, Integer> {

	/**
	 * Creates new user.
	 * 
	 * @param user
	 * @return created user
	 */
	@Transactional
	User save(User domain);
	
	/**
	 * Finds user by id.
	 * @param id
	 * @return Optional contains user or empty in case user does not exist.
	 */
	Optional<User> findById(Integer id);
	
	/**
	 * Check if user exists.
	 * @param id
	 * @return true if exists, otherwise false.
	 */
	Boolean existsById(Integer id);

}
