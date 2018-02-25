package com.tide.interview.service;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.tide.interview.dto.AnnouncementDto;
import com.tide.interview.dto.VoteResultDto;
import com.tide.interview.exception.DomainObjectNotFoundException;
import com.tide.interview.persistence.domain.Announcement;
import com.tide.interview.persistence.domain.VoteResult;
import com.tide.interview.persistence.repository.AnnouncementRepository;
import com.tide.interview.persistence.repository.VoteResultRepository;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementServiceDefaultImplUnitTest {

	private static final String SUBJECT = "TestSubject";
	
	private static final String SUMMARY = "TestSummary";
	
	private AnnouncementServiceDefaultImpl announcementService;
	
	@Mock
	private AnnouncementRepository announcementRepository;
	
	@Mock
	private VoteResultRepository voteResultRepository;	
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Before
	public void setUp() {
		announcementService = new AnnouncementServiceDefaultImpl(announcementRepository, voteResultRepository,
				modelMapper);
	}
	
	@Test
	public void testCreateAnnouncement() {
		Announcement announcement = mock(Announcement.class);
		AnnouncementDto announcementDto = new AnnouncementDto(SUBJECT, SUMMARY);
		when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

		announcementService.createAnnouncement(announcementDto);
		
		ArgumentCaptor<Announcement> argument = ArgumentCaptor.forClass(Announcement.class);
		verify(announcementRepository).save(argument.capture());
		assertEquals(announcementDto.getSubject(), argument.getValue().getSubject());
		assertEquals(announcementDto.getSummary(), argument.getValue().getSummary());
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testGetNonExistentAnnouncement() {
		when(announcementRepository.findById(1)).thenReturn(Optional.empty());
		
		announcementService.getAnnouncementDto(1);
	}
	
	@Test
	public void testGetExistingAnnouncement() {
		Announcement announcement = new Announcement(1, SUBJECT, SUMMARY);
		when(announcementRepository.findById(1)).thenReturn(Optional.of(announcement));
		
		AnnouncementDto announcementDto = announcementService.getAnnouncementDto(1);
		
		assertEquals(announcement.getId(), announcementDto.getId());
		assertEquals(announcement.getSubject(), announcementDto.getSubject());
		assertEquals(announcement.getSummary(), announcementDto.getSummary());
	}
	
	@Test(expected = DomainObjectNotFoundException.class)
	public void testGetVoteResultOfNonExistentAnnouncement() {
		when(voteResultRepository.findByAnnouncementId(1)).thenReturn(Optional.empty());
		
		announcementService.getVoteResult(1);
	}
	
	@Test
	public void testGetVoteResultOfExistingAnnouncement() {
		VoteResult voteResult = new VoteResult(4, 10);
		when(voteResultRepository.findByAnnouncementId(1)).thenReturn(Optional.of(voteResult));
		
		VoteResultDto voteResultDto = announcementService.getVoteResult(1);
		
		assertEquals(voteResult.getLikes(), voteResultDto.getLikes());
		assertEquals(voteResult.getDislikes(), voteResultDto.getDislikes());
	}
}
