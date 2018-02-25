package com.tide.interview.service;

import com.tide.interview.dto.VoteDto;
import com.tide.interview.dto.VoteDtoSet;

/**
 * Service contains method related to vote business logic.
 */
public interface VoteService {
	
	/**
	 * Gets votes for announcement.
	 * 
	 * @param announcementId announcement id.
	 * @return vote data transfer object set.
	 */
	VoteDtoSet getVotesByAnnouncementId(int announcementId);
	
	/**
	 * Creates vote.
	 * 
	 * @param voteDto vote data transfer object
	 * @return id of created vote
	 */
	Integer createVote(VoteDto voteDto);

	/**
	 * Gets vote.
	 * 
	 * @param voteId vote id.
	 * @return vote data transfer object.
	 */
	VoteDto getVoteDto(int voteId);
	
}
