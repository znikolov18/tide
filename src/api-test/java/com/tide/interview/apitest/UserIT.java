package com.tide.interview.apitest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.tide.interview.dto.UserDto;

public class UserIT extends BaseIT {

    private static final String USER_URI = "/users";
    
	private static final String NON_EXISTENT_ID_URI = "/-1";

	@Test
    public void testAvailability() {
        given().when().get(USER_URI + "/1").then().statusCode(200);
    }
    
    @Test
    public void testCreateUserWithInvalidNameMaxSize() {
    	UserDto userDto = new UserDto(StringUtils.leftPad("name", 100, '*'));
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(userDto)
    			.when().post(USER_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
        
        assertFalse(errors.isEmpty());
        assertThat(errors, contains("name: size must be between 2 and 45"));
    }
    
    @Test
    public void testCreateUserWithInvalidSubjectMinSize() {
    	UserDto userDto = new UserDto("");
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(userDto)
    			.when().post(USER_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("name: size must be between 2 and 45"));
    }
    
    @Test
    public void testCreateUserWithInvalidSubjectMissing() {
    	UserDto userDto = new UserDto();
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(userDto)
    			.when().post(USER_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("name: must not be null"));
    }
      
	@Test
	public void testCreateUserAndGetNewlyCreatedUser() {
		Integer createdUserId = createUser("TestName");
		
		UserDto createdUser = RestAssured.get(USER_URI + "/" + createdUserId).body().as(UserDto.class);

		assertNotNull(createdUser);
		assertEquals(createdUser.getName(), "TestName");
	}
	   
    @Test
    public void testGetNonExistentUser() {
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.when().get(USER_URI + NON_EXISTENT_ID_URI)
    			.then().statusCode(404)
    			.extract().path("errors");

    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("user with id -1 could not be found"));
    }
    
}
