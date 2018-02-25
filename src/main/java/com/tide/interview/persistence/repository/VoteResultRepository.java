package com.tide.interview.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.tide.interview.persistence.domain.VoteResult;

public interface VoteResultRepository extends Repository<VoteResult, Integer> {

	/**
	 * Finds vote result by announcement id.
	 * @param id
	 * @return Optional contains vote result or empty in case announcement does not exist.
	 */
	Optional<VoteResult> findByAnnouncementId(Integer id);
	
}
