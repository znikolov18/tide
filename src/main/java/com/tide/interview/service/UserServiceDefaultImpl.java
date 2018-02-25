package com.tide.interview.service;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tide.interview.dto.UserDto;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.persistence.domain.User;
import com.tide.interview.persistence.repository.UserRepository;

/**
 * Implementation of {@link UserService}
 */
@Service
public class UserServiceDefaultImpl implements UserService {

	private final static Logger LOGGER = Logger.getLogger(UserServiceDefaultImpl.class);
	
	private ModelMapper modelMapper;
	
	private UserRepository userRepository;
	
	@Autowired
	public UserServiceDefaultImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public UserDto getUserDto(int userId) throws DomainObjectNotFoundException {
		User user = userRepository.findById(userId).<DomainObjectNotFoundException>orElseThrow(() -> {
			LOGGER.info(String.format("User with id %d was not found.", userId));
			throw new DomainObjectNotFoundException("user", userId);
		});
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public Integer createUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		Integer userId = userRepository.save(user).getId();
		LOGGER.info(String.format("Created an user with id %d", userId));
		
		return userId;
	}
		
	@Override
	public Boolean checkIfUserExists(int userId) {
		return userRepository.existsById(userId);
	}

}
