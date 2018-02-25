package com.tide.interview.apitest;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.UserDto;
import com.tide.interview.dto.VoteDto;

public class BaseIT {
	
	private static final String ANNOUNCEMENT_URI = "/announcements";

	private static final String USER_URI = "/users";

	private static final String VOTE_URI = "/votes";

	@BeforeClass
	public static void setup() {		
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 58090;
		RestAssured.basePath = "/TideInterview";		
		RestAssured.defaultParser = Parser.JSON;
	}

	public Integer createAnnouncement(String subject, String summary) {
		AnnouncementDto announcementDto = new AnnouncementDto(subject, summary);
		String locationHeader = given().contentType(MediaType.APPLICATION_JSON).body(announcementDto).when()
				.post(ANNOUNCEMENT_URI).then().statusCode(201).extract().header("Location");
		return getIdFromLocationHeader(locationHeader);
	}

	public Integer createUser(String name) {
		UserDto userDto = new UserDto(name);
		String locationHeader = given().contentType(MediaType.APPLICATION_JSON).body(userDto).when().post(USER_URI)
				.then().statusCode(201).extract().header("Location");
		return getIdFromLocationHeader(locationHeader);
	}

	private Integer getIdFromLocationHeader(String locationHeader) {
		String[] urlParts = locationHeader.split("/");
		return new Integer(urlParts[urlParts.length - 1]);
	}

	public Integer createVote(Integer announcementId, Integer userId, Boolean positive) {
		VoteDto voteDto = new VoteDto(announcementId, userId, positive);
		String locationHeader = given().contentType(MediaType.APPLICATION_JSON).body(voteDto).when()
				.post(VOTE_URI).then().statusCode(201).extract().header("Location");
		return getIdFromLocationHeader(locationHeader);
	}

	public List<String> createInvalidVote(Integer announcementId, Integer userId, Boolean positive,
			Integer statusCode) {
		VoteDto voteDto = new VoteDto(announcementId, userId, positive);
		return given().contentType(MediaType.APPLICATION_JSON).body(voteDto).when()
				.post(VOTE_URI).then().statusCode(statusCode)
				.extract().path("errors");
	}

}