package com.tide.interview.persistence.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.Repository;

import com.tide.interview.persistence.domain.Announcement;

/**
 * Repository for announcement related operations.
 */
public interface AnnouncementRepository extends Repository<Announcement, Integer> {

	/**
	 * Creates new announcement.
	 * 
	 * @param announcement
	 * @return created announcement
	 */
	@Transactional
	Announcement save(Announcement announcement);	
	
	/**
	 * Finds announcement by id.
	 * @param id
	 * @return Optional contains announcement or empty in case announcement does not exist.
	 */
	Optional<Announcement> findById(Integer id);
	
	/**
	 * Check if announcement exists.
	 * @param id
	 * @return true if exists, otherwise false.
	 */
	Boolean existsById(Integer id);

}
