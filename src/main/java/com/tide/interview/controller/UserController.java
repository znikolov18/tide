package com.tide.interview.controller;

import java.net.URI;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tide.interview.dto.UserDto;
import com.tide.interview.service.UserService;

/**
 * Controller for user related requests
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

	private final static Logger LOGGER = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	/**
	 *  Returns user.
	 *  
	 * @param userId user id
	 * @return user and status 200 OK
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable int userId) {
		LOGGER.info(String.format("Getting an user with id %s", userId));
		return new ResponseEntity<>(userService.getUserDto(userId), HttpStatus.OK);
	}

	/**
	 * Creates user.
	 * 
	 * @param userDto user data transfer object
	 * @param uriComponentsBuilder uri components builder
	 * @return empty response with status 201 CREATED
	 */
	@PostMapping
	public ResponseEntity<Void> addUser(@Valid @RequestBody UserDto userDto, UriComponentsBuilder uriComponentsBuilder) {
		LOGGER.info("Creating new user");
		Integer id = userService.createUser(userDto);
		URI locationUri = uriComponentsBuilder.path("/users/").path(id.toString()).build().toUri();

		return ResponseEntity.created(locationUri).build();
	}

}
