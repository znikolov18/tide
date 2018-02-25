package com.tide.interview.apitest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.VoteDto;
import com.tide.interview.dto.VoteDtoSet;
import com.tide.interview.dto.VoteResultDto;

public class AnnouncementIT extends BaseIT {

	private static final String NON_EXISTENT_ID_URI = "/-1";

	private static final String ANNOUNCEMENT_URI = "/announcements";
	
	private static final String VOTE_URI = "/votes";
	
	private static final String VOTE_RESULT_URI = "/vote-result";

	@Test
    public void testAvailability() {
        given().when().get(ANNOUNCEMENT_URI + "/1").then().statusCode(200);
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSubjectMaxSize() {
    	AnnouncementDto announcementDto = new AnnouncementDto(StringUtils.leftPad("subject", 200, '*'), "Summary");
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
        
        assertFalse(errors.isEmpty());
        assertThat(errors, contains("subject: size must be between 2 and 100"));
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSubjectMinSize() {
    	AnnouncementDto announcementDto = new AnnouncementDto("", "Summary");
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("subject: size must be between 2 and 100"));
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSubjectMissing() {
    	AnnouncementDto announcementDto = new AnnouncementDto(null, "Summary");
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("subject: must not be null"));
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSummaryMaxSize() {
    	AnnouncementDto announcementDto = new AnnouncementDto("Subject", StringUtils.leftPad("summary", 600, '*'));
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("summary: size must be between 2 and 500"));
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSummaryMinSize() {
    	AnnouncementDto announcementDto = new AnnouncementDto("Subject", "");
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("summary: size must be between 2 and 500"));
    }
    
    @Test
    public void testCreateAnnouncementWithInvalidSummaryMissing() {
    	AnnouncementDto announcementDto = new AnnouncementDto("Subject", null);
    	
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.body(announcementDto)
    			.when().post(ANNOUNCEMENT_URI)
    			.then().statusCode(400)
    			.extract().path("errors");

    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("summary: must not be null"));
    }
    
	@Test
	public void testCreateAnnouncementAndGetNewlyCreatedAnnouncement() {
		Integer createdAnnouncementId = createAnnouncement("TestSubject", "TestSummary");
		
		AnnouncementDto createdAnnouncement = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId).body()
				.as(AnnouncementDto.class);

		assertEquals(createdAnnouncement.getSubject(), "TestSubject");
		assertEquals(createdAnnouncement.getSummary(), "TestSummary");		
	}
   
    @Test
    public void testGetNonExistentAnnouncement() {
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.when().get(ANNOUNCEMENT_URI + NON_EXISTENT_ID_URI)
    			.then().statusCode(404)
    			.extract().path("errors");

    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("announcement with id -1 could not be found"));
    }
    

    
    @Test
    public void testGetVoteResultForNonExistentAnnouncement() {
    	List<String> errors = given().contentType(MediaType.APPLICATION_JSON)
    			.when().get(ANNOUNCEMENT_URI + NON_EXISTENT_ID_URI + VOTE_RESULT_URI)
    			.then().statusCode(404)
    			.extract().path("errors");
    	
    	assertFalse(errors.isEmpty());
    	assertThat(errors, contains("announcement with id -1 could not be found"));
    }
    
	@Test
	public void testCreateDuplicateVoteForAnnouncementAndGetVoteResult() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");
		Integer createdUserId = createUser("VotedUser");

		createVote(createdAnnouncementId, createdUserId, false);
		List<String> errors = createInvalidVote(createdAnnouncementId, createdUserId, true, 409);

		assertFalse(errors.isEmpty());
		assertThat(errors, contains(String.format("vote for announcement with id %d by user with id %d already exists",
				createdAnnouncementId, createdUserId)));

		VoteDtoSet votesOfCreatedAnnouncement = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", createdAnnouncementId)
				.get(VOTE_URI).body().as(VoteDtoSet.class);

		assertNotNull(votesOfCreatedAnnouncement);

		Set<VoteDto> voteDtos = votesOfCreatedAnnouncement.getVotes();

		assertFalse(voteDtos.isEmpty());
		assertEquals(voteDtos.size(), 1);

		VoteDto createdVoteDto = voteDtos.iterator().next();

		assertEquals(createdVoteDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(createdVoteDto.getUserId(), createdUserId);
		assertEquals(createdVoteDto.isPositive(), false);

		VoteResultDto voteResultDto = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId + VOTE_RESULT_URI)
				.body().as(VoteResultDto.class);

		assertNotNull(voteResultDto);
		assertEquals(voteResultDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(voteResultDto.getLikes(), Integer.valueOf(0));
		assertEquals(voteResultDto.getDislikes(), Integer.valueOf(1));
	}

	@Test
	public void testGetVotesAndVoteResultForAnnouncementWithoutVotes() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithoutVotes",
				"AnnouncementWithoutVotesSummary");

		VoteDtoSet votesOfCreatedAnnouncement = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", createdAnnouncementId)
				.get(VOTE_URI).body().as(VoteDtoSet.class);

		assertNotNull(votesOfCreatedAnnouncement);
		assertTrue(votesOfCreatedAnnouncement.getVotes().isEmpty());

		VoteResultDto voteResultDto = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId + VOTE_RESULT_URI)
				.body().as(VoteResultDto.class);

		assertNotNull(voteResultDto);
		assertEquals(voteResultDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(voteResultDto.getLikes(), Integer.valueOf(0));
		assertEquals(voteResultDto.getDislikes(), Integer.valueOf(0));
	}

	@Test
	public void testCreatePositiveVoteForAnnouncementAndGetVoteResult() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");
		Integer createdUserId = createUser("VotedUser");

		createVote(createdAnnouncementId, createdUserId, true);

		VoteDtoSet votesOfCreatedAnnouncement = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", createdAnnouncementId)
				.get(VOTE_URI).body().as(VoteDtoSet.class);

		assertNotNull(votesOfCreatedAnnouncement);

		Set<VoteDto> voteDtos = votesOfCreatedAnnouncement.getVotes();

		assertFalse(voteDtos.isEmpty());
		assertEquals(voteDtos.size(), 1);

		VoteDto createdVoteDto = voteDtos.iterator().next();

		assertEquals(createdVoteDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(createdVoteDto.getUserId(), createdUserId);
		assertEquals(createdVoteDto.isPositive(), true);

		VoteResultDto voteResultDto = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId + VOTE_RESULT_URI)
				.body().as(VoteResultDto.class);

		assertNotNull(voteResultDto);
		assertEquals(voteResultDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(voteResultDto.getLikes(), Integer.valueOf(1));
		assertEquals(voteResultDto.getDislikes(), Integer.valueOf(0));
	}

	@Test
	public void testCreateManyVotesForAnnouncementAndGetVoteResult() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithManyVotes",
				"AnnouncementWithManyVotesSummary");

		IntStream.rangeClosed(1, 4).forEach(index -> {
			Integer userId = createUser("VotedUser" + index);
			createVote(createdAnnouncementId, userId, true);
		});

		IntStream.rangeClosed(5, 8).forEach(index -> {
			Integer userId = createUser("VotedUser" + index);
			createVote(createdAnnouncementId, userId, false);
		});

		VoteDtoSet votesOfCreatedAnnouncement = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", createdAnnouncementId)
				.get(VOTE_URI).body().as(VoteDtoSet.class);

		assertNotNull(votesOfCreatedAnnouncement);

		Set<VoteDto> voteDtos = votesOfCreatedAnnouncement.getVotes();

		assertFalse(voteDtos.isEmpty());
		assertEquals(voteDtos.size(), 8);

		VoteResultDto voteResultDto = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId + VOTE_RESULT_URI)
				.body().as(VoteResultDto.class);

		assertNotNull(voteResultDto);
		assertEquals(voteResultDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(voteResultDto.getLikes(), Integer.valueOf(4));
		assertEquals(voteResultDto.getDislikes(), Integer.valueOf(4));
	}

	@Test
	public void testCreateNegativeVoteForAnnouncementAndGetVoteResult() {
		Integer createdAnnouncementId = createAnnouncement("AnnouncementWithVotes", "AnnouncementWithVotesSummary");
		Integer createdUserId = createUser("VotedUser");

		createVote(createdAnnouncementId, createdUserId, false);

		VoteDtoSet votesOfCreatedAnnouncement = given().contentType(MediaType.APPLICATION_JSON)
				.queryParam("announcementId", createdAnnouncementId)
				.get(VOTE_URI).body().as(VoteDtoSet.class);

		assertNotNull(votesOfCreatedAnnouncement);

		Set<VoteDto> voteDtos = votesOfCreatedAnnouncement.getVotes();

		assertFalse(voteDtos.isEmpty());
		assertEquals(voteDtos.size(), 1);

		VoteDto createdVoteDto = voteDtos.iterator().next();

		assertEquals(createdVoteDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(createdVoteDto.getUserId(), createdUserId);
		assertEquals(createdVoteDto.isPositive(), false);

		VoteResultDto voteResultDto = RestAssured.get(ANNOUNCEMENT_URI + "/" + createdAnnouncementId + VOTE_RESULT_URI)
				.body().as(VoteResultDto.class);

		assertNotNull(voteResultDto);
		assertEquals(voteResultDto.getAnnouncementId(), createdAnnouncementId);
		assertEquals(voteResultDto.getLikes(), Integer.valueOf(0));
		assertEquals(voteResultDto.getDislikes(), Integer.valueOf(1));
	}

}
