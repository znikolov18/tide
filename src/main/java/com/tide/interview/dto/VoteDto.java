package com.tide.interview.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * Vote data transfer object.
 */
public class VoteDto {

	private Integer id;
	
	@NotNull
	private Integer announcementId;
	
	private String announcementSubject;
	
	@NotNull
	private Integer userId;
	
	private String userName;
	
	@NotNull
	private Boolean positive;

	public VoteDto() {
	}
	
	public VoteDto(Integer announcementId, Integer userId, Boolean positive) {
		this.announcementId = announcementId;
		this.userId = userId;
		this.positive = positive;
	}

	public VoteDto(Integer id, Integer announcementId, String announcementSubject, Integer userId, String userName,
			Boolean positive) {
		this.id = id;
		this.announcementId = announcementId;
		this.announcementSubject = announcementSubject;
		this.userId = userId;
		this.userName = userName;
		this.positive = positive;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}

	public String getAnnouncementSubject() {
		return announcementSubject;
	}

	public void setAnnouncementSubject(String announcementSubject) {
		this.announcementSubject = announcementSubject;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean isPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		VoteDto voteDto = (VoteDto) object;
		return Objects.equals(this.id, voteDto.getId())
				&& Objects.equals(this.announcementId, voteDto.getAnnouncementId())
				&& Objects.equals(this.userId, voteDto.getUserId())
				&& Objects.equals(this.positive, voteDto.isPositive());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.announcementId, this.userId, this.positive);
	}

	@Override
	public String toString() {
		return new StringBuilder("VoteDto [id=").append(id).append(", announcementId=").append(this.announcementId)
				.append(", announcementSubject=").append(this.announcementSubject).append(", userId=")
				.append(this.userId).append(", userName=").append(this.userName).append(", positive=")
				.append(this.positive).append("]").toString();
	}
	
}
