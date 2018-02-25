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

import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.VoteResultDto;
import com.tide.interview.service.AnnouncementService;

/**
 * Controller for announcement related requests
 */
@RestController
@RequestMapping(value = "/announcements")
public class AnnouncementController {

	private final static Logger LOGGER = Logger.getLogger(AnnouncementController.class);
	
	@Autowired
	private AnnouncementService announcementService;
	
	/**
	 *  Returns announcement.
	 *  
	 * @param announcementId announcement id
	 * @return announcement and status 200 OK
	 */
	@GetMapping("/{announcementId}")
	public ResponseEntity<AnnouncementDto> getAnnouncement(@PathVariable int announcementId) {
		LOGGER.info(String.format("Getting an announcement with id %s", announcementId));
		return new ResponseEntity<>(announcementService.getAnnouncementDto(announcementId), HttpStatus.OK);
	}
    
	/**
	 * Creates announcement.
	 * 
	 * @param announcementDto announcement data transfer object
	 * @param uriComponentsBuilder uri components builder
	 * @return empty response with status 201 CREATED
	 */
	@PostMapping
	public ResponseEntity<Void> addAnnouncement(@Valid @RequestBody AnnouncementDto announcementDto,
			UriComponentsBuilder uriComponentsBuilder) {
		LOGGER.info("Creating new announcement");
		Integer id = announcementService.createAnnouncement(announcementDto);
		URI locationUri = uriComponentsBuilder.path("/announcements/").path(id.toString()).build().toUri();

		return ResponseEntity.created(locationUri).build();
	}

	/**
	 * Returns vote result for announcement.
	 * 
	 * @param announcementId announcement id
	 * @return vote result data transfer object and 200 OK
	 */
	@GetMapping("/{announcementId}/vote-result")
	public ResponseEntity<VoteResultDto> getAnnouncementVoteResult(@PathVariable int announcementId) {
		LOGGER.info(String.format("Getting the vote results for announcement with id %s", announcementId));
		return new ResponseEntity<>(announcementService.getVoteResult(announcementId), HttpStatus.OK);
	}
	
}
