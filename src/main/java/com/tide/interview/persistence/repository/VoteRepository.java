package com.tide.interview.persistence.repository;

import java.util.Optional;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.Repository;

import com.tide.interview.persistence.domain.Vote;

public interface VoteRepository extends Repository<Vote, Integer> {

	/**
	 * Creates new vote.
	 * 
	 * @param vote
	 * @return created vote
	 */
	@Transactional
	Vote save(Vote domain) throws DataIntegrityViolationException;
	
	/**
	 * Finds vote by id.
	 * @param id
	 * @return Optional contains vote or empty in case vote does not exist.
	 */
	Optional<Vote> findById(Integer id);
	
	/**
	 * Creates stream of votes for announcement.
	 * @param announcementId announcement id
	 * @return stream of votes
	 */
	Stream<Vote> findAllByAnnouncementId(Integer announcementId);
	
	/**
	 * Check if vote existence by announcement id and user id.
	 * @param announcementId announcement id
	 * @param userId user id
	 * @return true if exists, otherwise false
	 */
	Boolean existsByAnnouncementIdAndUserId(Integer announcementId, Integer userId);
	
}
