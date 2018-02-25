package com.tide.interview.service;

import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.VoteResultDto;

/**
 * Service contains method related to announcement business logic.
 */
public interface AnnouncementService {
	
	/**
	 * Gets announcement.
	 * 
	 * @param announcementId announcement id.
	 * @return announcement data transfer object.
	 */
	AnnouncementDto getAnnouncementDto(int announcementId);

	/**
	 * Creates announcement.
	 * 
	 * @param announcementDto announcement data transfer object
	 * @return id of created announcement
	 */
	Integer createAnnouncement(AnnouncementDto announcementDto);

	/**
	 * Checks announcement existence.
	 * 
	 * @param announcementId announcement id
	 * @return true if exists, otherwise false
	 */
	Boolean checkIfAnnouncementExists(int announcementId);
	
	
	/**
	 * Gets vote result for announcement
	 * @param announcementId announcement id
	 * @return vote result data transfer object
	 */
	VoteResultDto getVoteResult(int announcementId);
	
}
