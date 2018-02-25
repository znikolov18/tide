package com.tide.interview.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User data transfer object.
 */
public class UserDto {

	private Integer id;
	
	@NotNull
    @Size(min=2, max=45)
	private String name;

	public UserDto() {
	}
	
	public UserDto(String name) {
		this.name = name;
	}
	
	public UserDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		UserDto userDto = (UserDto) object;
		return Objects.equals(this.id, userDto.getId()) && Objects.equals(this.name, userDto.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name);
	}

	@Override
	public String toString() {
		return new StringBuilder("UserDto [id=").append(id)
				.append(", name=").append(name).append("]").toString();
	}
}
