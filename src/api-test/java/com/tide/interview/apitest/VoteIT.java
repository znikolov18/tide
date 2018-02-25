package com.tide.interview.apitest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.tide.interview.dto.VoteDto;

public class VoteIT extends BaseIT {

	private static final String VOTE_URI = "/votes";
	
	private static final String NON_EXISTENT_ID_URI = "/-1";

	@Test
    public void testAvailability() {
        given().when().get(VOTE_URI + "/1").then().statusCode(200);
    }
	
	@Test
	public void testGetNonExistentVote() {
		List<String> errors = given().contentType(MediaType.APPLICATION_JSON).when()
				.get(VOTE_URI + NON_EXISTENT_ID_URI).then().statusCode(404).extract()
				.path("errors");

		assertFalse(errors.isEmpty());
		assertThat(errors, contains("vote with id -1 could not be found"));
	}

	@Test
	public void testGetExistingVote() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVote", "AnnouncementWithVoteSummary");
		Integer createdUserId = createUser("VotedUser");

		Integer createdVoteId = createVote(createdAnnouncementId, createdUserId, true);

		VoteDto createdVoteDto = RestAssured
				.get(VOTE_URI + "/" + createdVoteId).body()
				.as(VoteDto.class);

		assertEquals(createdVoteDto.getId(), createdVoteId);
		assertEquals(createdVoteDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(createdVoteDto.getAnnouncementSubject(), "AnnouncementWithVote");
		assertEquals(createdVoteDto.getUserId(), createdUserId);
		assertEquals(createdVoteDto.getUserName(), "VotedUser");
	}

	@Test
	public void testGetVotesForNonExistentAnnouncement() {
		List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", "-1")
				.when().get(VOTE_URI)
				.then().statusCode(404)
				.extract().path("errors");

		assertFalse(errors.isEmpty());
		assertThat(errors, contains("announcement with id -1 could not be found"));
	}
	
	@Test
	public void testGetVotesWithMissingAnnouncement() {
		given().contentType(MediaType.APPLICATION_JSON)
				.when().get(VOTE_URI)
				.then().statusCode(400);
	}

	@Test
	public void testCreateVoteWithMissingAnnouncementId() {
		Integer createdUserId = createUser("VotedUser");
		List<String> errors = createInvalidVote(null, createdUserId, false, 400);
		assertFalse(errors.isEmpty());
		assertThat(errors, contains("announcementId: must not be null"));
	}

	@Test
	public void testCreateVoteWithMissingUserId() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");
		List<String> errors = createInvalidVote(createdAnnouncementId, null, false, 400);
		assertFalse(errors.isEmpty());
		assertThat(errors, contains("userId: must not be null"));
	}

	@Test
	public void testCreateVoteWithMissingPositive() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");
		Integer createdUserId = createUser("VotedUser");
		List<String> errors = createInvalidVote(createdAnnouncementId, createdUserId, null, 400);
		assertFalse(errors.isEmpty());
		assertThat(errors, contains("positive: must not be null"));
	}

	@Test
	public void testCreateVoteForNonExistentAnnouncement() {
		Integer createdUserId = createUser("VotedUser");

		List<String> errors = createInvalidVote(-1, createdUserId, false, 404);
		assertFalse(errors.isEmpty());
		assertThat(errors, contains("announcement with id -1 could not be found"));
	}

	@Test
	public void testCreateVoteForNonExistentUser() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");

		List<String> errors = createInvalidVote(createdAnnouncementId, -1, false, 404);
		assertFalse(errors.isEmpty());
		assertThat(errors, contains("user with id -1 could not be found"));
	}

}
