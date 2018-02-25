package com.tide.interview.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.tide.interview.dto.UserDto;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.persistence.domain.User;
import com.tide.interview.persistence.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceDefaultImplUnitTest {

	private UserServiceDefaultImpl userService;
	
	@Mock
	private UserRepository userRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Before
	public void setUp() {
		userService = new UserServiceDefaultImpl(userRepository, modelMapper);
	}
	
	@Test
	public void testCreateUser() {
		User user = mock(User.class);
		UserDto userDto = new UserDto(null, "TestUser");
		when(userRepository.save(any(User.class))).thenReturn(user);

		userService.createUser(userDto);
		
		ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(argument.capture());
		assertEquals(userDto.getName(), argument.getValue().getName());
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testGetNonExistingUser() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		
		userService.getUserDto(1);
	}
	
	@Test
	public void testGetExistingUser() {
		User user = new User(1, "TestUser");
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		UserDto userDto = userService.getUserDto(1);
		
		assertEquals(user.getId(), userDto.getId());
		assertEquals(user.getName(), userDto.getName());
	}
}
