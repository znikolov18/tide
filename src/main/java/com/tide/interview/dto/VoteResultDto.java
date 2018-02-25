package com.tide.interview.dto;

import java.util.Objects;

/**
 * Vote result data transfer object.
 */
public class VoteResultDto {

	private Integer announcementId;
	private Integer likes;
	private Integer dislikes;

	public Integer getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		VoteResultDto voteResultDto = (VoteResultDto) object;
		return Objects.equals(this.announcementId, voteResultDto.getAnnouncementId())
				&& Objects.equals(this.likes, voteResultDto.getLikes())
				&& Objects.equals(this.dislikes, voteResultDto.getDislikes());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.announcementId, this.likes, this.dislikes);
	}

	@Override
	public String toString() {
		return new StringBuilder("VoteResultDto [announcementId=").append(this.announcementId)
				.append(", likes=").append(this.likes)
				.append(", dislikes=").append(this.dislikes).append("]").toString();
	}

}
