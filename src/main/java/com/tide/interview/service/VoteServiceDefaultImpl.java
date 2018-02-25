package com.tide.interview.service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tide.interview.dto.VoteDto;
import com.tide.interview.dto.VoteDtoSet;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.exception.VoteDuplicationException;
import com.tide.interview.persistence.domain.Vote;
import com.tide.interview.persistence.repository.VoteRepository;

/**
 * Implementation of {@link VoteService}
 */
@Service
public class VoteServiceDefaultImpl implements VoteService {

	private final static Logger LOGGER = Logger.getLogger(VoteServiceDefaultImpl.class);
	
	private VoteRepository voteRepository;
	
	private AnnouncementService announcementService;
	
	private UserService userService;
	
	private ModelMapper modelMapper;

	@Autowired
	public VoteServiceDefaultImpl(VoteRepository voteRepository, AnnouncementService announcementService,
			UserService userService, ModelMapper modelMapper) {
		this.voteRepository = voteRepository;
		this.announcementService = announcementService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@Override
	@Transactional
	public VoteDtoSet getVotesByAnnouncementId(int announcementId) {
		if(!announcementService.checkIfAnnouncementExists(announcementId)) {
			throw new DomainObjectNotFoundException("announcement", announcementId);
		}
		Stream<Vote> voteStream = voteRepository.findAllByAnnouncementId(announcementId);
		Set<VoteDto> voteDtos = voteStream.map(vote -> modelMapper.map(vote, VoteDto.class))
				.collect(Collectors.toSet());
		return new VoteDtoSet(voteDtos);
	}
	
	@Override
	@Transactional
	public VoteDto getVoteDto(int voteId) {
		Vote vote = voteRepository.findById(voteId)
				.<DomainObjectNotFoundException>orElseThrow(() -> {
					throw new DomainObjectNotFoundException("vote", voteId);
				});
		return modelMapper.map(vote, VoteDto.class);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Integer createVote(VoteDto voteDto) {
		validateDataIntegrity(voteDto.getAnnouncementId(), voteDto.getUserId());
		
		Vote vote = modelMapper.map(voteDto, Vote.class);
		Integer voteId = voteRepository.save(vote).getId();
		LOGGER.info(String.format("Created a vote with id %d.", voteId));
		
		return voteId;
	}
	
	private void validateDataIntegrity(Integer announcementId, Integer userId) {
		if (!announcementService.checkIfAnnouncementExists(announcementId)) {
			LOGGER.info(String.format("Announcement with id %d was not found.", announcementId));
			throw new DomainObjectNotFoundException("announcement", announcementId);
		}

		if (!userService.checkIfUserExists(userId)) {
			LOGGER.info(String.format("User with id %d was not found.", userId));
			throw new DomainObjectNotFoundException("user", userId);
		}

		if (voteRepository.existsByAnnouncementIdAndUserId(announcementId, userId)) {
			LOGGER.info(String.format("Vote for announcement with id %d by user with id %d already exists.",
					announcementId, userId));
			throw new VoteDuplicationException(announcementId, userId);
		}
	}

}
