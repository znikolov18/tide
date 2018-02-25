package com.tide.interview.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.google.common.collect.Sets;
import com.tide.interview.dto.VoteDto;
import com.tide.interview.dto.VoteDtoSet;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.exception.VoteDuplicationException;
import com.tide.interview.persistence.domain.Announcement;
import com.tide.interview.persistence.domain.User;
import com.tide.interview.persistence.domain.Vote;
import com.tide.interview.persistence.repository.VoteRepository;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceDefaultImplUnitTest {

	private VoteServiceDefaultImpl voteService;
	
	@Mock	
	private VoteRepository voteRepository;
	
	@Mock
	AnnouncementService announcementService;
	
	@Mock
	UserService userService;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Before
	public void setUp() {
		voteService = new VoteServiceDefaultImpl(voteRepository, announcementService, userService, modelMapper);
	}
		
	@Test(expected = DomainObjectNotFoundException.class)
	public void testGetNonExistentVote() {
		when(voteRepository.findById(1)).thenReturn(Optional.empty());
		
		voteService.getVoteDto(1);
	}
	
	@Test
	public void testGetExistingVote() {
		Announcement announcement = new Announcement(1, "AnnouncementSubject", "AnnouncementSummary");
		User user = new User(1, "TestUser");
		Vote vote = new Vote(1, announcement, user, true);
		when(voteRepository.findById(1)).thenReturn(Optional.of(vote));
		
		VoteDto voteDto = voteService.getVoteDto(1);
		
		assertEquals(vote.getId(), voteDto.getId());
		assertEquals(announcement.getId(), voteDto.getAnnouncementId());
		assertEquals(announcement.getSubject(), voteDto.getAnnouncementSubject());
		assertEquals(user.getId(), voteDto.getUserId());
		assertEquals(user.getName(), voteDto.getUserName());
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testCreateVoteWithNonExistentAnnouncementId() {
		VoteDto voteDto = mock(VoteDto.class);
		when(voteDto.getAnnouncementId()).thenReturn(1);
		when(announcementService.checkIfAnnouncementExists(1)).thenReturn(false);
		voteService.createVote(voteDto);
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testCreateVoteWithNonExistentUserId() {
		VoteDto voteDto = mock(VoteDto.class);
		when(voteDto.getAnnouncementId()).thenReturn(1);
		when(voteDto.getUserId()).thenReturn(1);
		when(userService.checkIfUserExists(1)).thenReturn(false);
		voteService.createVote(voteDto);
	}
	
	@Test(expected = VoteDuplicationException.class)
	public void testCreateDuplicateVote() {
		VoteDto voteDto = mock(VoteDto.class);
		when(voteDto.getAnnouncementId()).thenReturn(1);
		when(voteDto.getUserId()).thenReturn(1);
		when(announcementService.checkIfAnnouncementExists(1)).thenReturn(true);
		when(userService.checkIfUserExists(1)).thenReturn(true);
		when(voteRepository.existsByAnnouncementIdAndUserId(1, 1)).thenReturn(true);
		voteService.createVote(voteDto);
	}
	
	@Test
	public void testCreateVote() {
		Vote vote = mock(Vote.class);
		VoteDto voteDto = new VoteDto(1, 1, "AnnouncementSubject", 1, "UserName", true);
		when(announcementService.checkIfAnnouncementExists(1)).thenReturn(true);
		when(userService.checkIfUserExists(1)).thenReturn(true);
		when(voteRepository.existsByAnnouncementIdAndUserId(1, 1)).thenReturn(false);
		when(voteRepository.save(any(Vote.class))).thenReturn(vote);

		voteService.createVote(voteDto);
		
		ArgumentCaptor<Vote> argument = ArgumentCaptor.forClass(Vote.class);
		verify(voteRepository).save(argument.capture());
		Vote persistedVote = argument.getValue();
		assertNotNull(persistedVote);
		assertEquals(voteDto.getAnnouncementId(), persistedVote.getAnnouncement().getId());
		assertEquals(voteDto.getAnnouncementSubject(), persistedVote.getAnnouncement().getSubject());
		assertEquals(voteDto.getUserId(), persistedVote.getUser().getId());
		assertEquals(voteDto.getUserName(), persistedVote.getUser().getName());
		assertEquals(voteDto.isPositive(), persistedVote.isPositive());
	}
	
	@Test
	public void testGetVotesByAnnouncementId() {
		Announcement announcement = new Announcement(1, "AnnouncementSubject", "AnnouncementSummary");
		User positiveUser = new User(1, "PositivieUser");
		User negativeUser = new User(2, "NegativeUser");
		Vote positiveVote = new Vote(1, announcement, positiveUser, true);
		Vote negativeVote = new Vote(2, announcement, negativeUser, false);
		Set<Vote> votes = Sets.newHashSet(positiveVote, negativeVote);
		when(announcementService.checkIfAnnouncementExists(1)).thenReturn(true);
		when(voteRepository.findAllByAnnouncementId(1)).thenReturn(votes.stream());
		
		VoteDtoSet voteDtoSet = voteService.getVotesByAnnouncementId(1);
		
		VoteDto expectedPositiveVoteDto = new VoteDto(1, 1, "AnnouncementSubject", 1, "PositiveUser", true);
		VoteDto expectedNegativeVoteDto = new VoteDto(2, 1, "AnnouncementSubject", 2, "NegativeUser", false);
		VoteDtoSet expectedVoteDtoSet = new VoteDtoSet(Sets.newHashSet(expectedPositiveVoteDto, expectedNegativeVoteDto)); 
		
		assertEquals(expectedVoteDtoSet, voteDtoSet);
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testGetVotesByNonExistentAnnouncementId() {
		when(announcementService.checkIfAnnouncementExists(1)).thenReturn(false);
		voteService.getVotesByAnnouncementId(1);
	}
	
}
