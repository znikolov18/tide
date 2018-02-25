package com.tide.interview.service;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.VoteResultDto;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.persistence.domain.Announcement;
import com.tide.interview.persistence.domain.VoteResult;
import com.tide.interview.persistence.repository.AnnouncementRepository;
import com.tide.interview.persistence.repository.VoteResultRepository;

/**
 * Implementation of {@link AnnouncementService}
 */
@Service
public class AnnouncementServiceDefaultImpl implements AnnouncementService {

	private final static Logger LOGGER = Logger.getLogger(AnnouncementServiceDefaultImpl.class);
	
	private AnnouncementRepository announcementRepository;
	
	private VoteResultRepository voteResultRepository;
	
	private ModelMapper modelMapper;
	
	@Autowired
	public AnnouncementServiceDefaultImpl(AnnouncementRepository announcementRepository,
			VoteResultRepository voteResultRepository, ModelMapper modelMapper) {
		this.announcementRepository = announcementRepository;
		this.voteResultRepository = voteResultRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public AnnouncementDto getAnnouncementDto(int announcementId) {
		Announcement announcement = announcementRepository.findById(announcementId)
				.<DomainObjectNotFoundException>orElseThrow(() -> {
					LOGGER.info(String.format("Announcement with id %d was not found.", announcementId));
					throw new DomainObjectNotFoundException("announcement", announcementId);
				});
		return modelMapper.map(announcement, AnnouncementDto.class);
	}

	@Override
	public Integer createAnnouncement(AnnouncementDto announcementDto) {
		Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
		Integer announcementId = announcementRepository.save(announcement).getId();
		LOGGER.info(String.format("Created an announcement with id %d", announcementId));
		
		return announcementId;
	}
	
	@Override
	public Boolean checkIfAnnouncementExists(int announcementId) {
		return announcementRepository.existsById(announcementId);
	}

	@Override
	public VoteResultDto getVoteResult(int announcementId) {
		VoteResult voteResult = voteResultRepository.findByAnnouncementId(announcementId)
				.<DomainObjectNotFoundException>orElseThrow(() -> {
					LOGGER.info(String.format("Announcement with id %d was not found.", announcementId));
					throw new DomainObjectNotFoundException("announcement", announcementId);
				});

		return modelMapper.map(voteResult, VoteResultDto.class);
	}

}
