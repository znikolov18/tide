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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tide.interview.dto.VoteDto;
import com.tide.interview.dto.VoteDtoSet;
import com.tide.interview.service.VoteService;

/**
 * Controller for vote related requests
 */
@RestController
@RequestMapping(value = "/votes")
public class VoteController {

	private final static Logger LOGGER = Logger.getLogger(VoteController.class);
	
	@Autowired
	private VoteService voteService;

	/**
	 *  Returns vote.
	 *  
	 * @param voteId vote id
	 * @return vote and status 200 OK
	 */
	@GetMapping("/{voteId}")
	public ResponseEntity<VoteDto> getVote(@PathVariable int voteId) {
		LOGGER.info(String.format("Getting a vote with id %s", voteId));
		return new ResponseEntity<>(voteService.getVoteDto(voteId), HttpStatus.OK);
	}

	/**
	 * Creates vote.
	 * 
	 * @param voteDto
	 *            vote data transfer object
	 * @param uriComponentsBuilder
	 *            uri components builder
	 * @return empty response with status 201 CREATED
	 */
	@PostMapping
	public ResponseEntity<Void> addVote(@Valid @RequestBody VoteDto voteDto,
			UriComponentsBuilder uriComponentsBuilder) {
		LOGGER.info(String.format("Adding a vote for announcement with id %s by user with id %s",
				voteDto.getAnnouncementId(), voteDto.getUserId()));
		Integer id = voteService.createVote(voteDto);
		String path = String.format("/votes/%d", id);
		URI locationUri = uriComponentsBuilder.path(path).build().toUri();

		return ResponseEntity.created(locationUri).build();
	}

	/**
	 * Returns votes for announcement.
	 * 
	 * @param announcementId
	 *            announcement id
	 * @return vote data transfer object set and status 200 OK
	 */
	@GetMapping
	public ResponseEntity<VoteDtoSet> getVotesForAnnouncement(
			@RequestParam(required = true, name = "announcementId") int announcementId) {
		LOGGER.info(String.format("Getting the votes for announcement with id %s", announcementId));
		return new ResponseEntity<>(voteService.getVotesByAnnouncementId(announcementId), HttpStatus.OK);
	}

}
