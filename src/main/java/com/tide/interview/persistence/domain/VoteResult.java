package com.tide.interview.persistence.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Vote domain object.
 */
@Entity
@Immutable
@Table(name = "vote_result_vw")
public class VoteResult implements java.io.Serializable {

	private Integer announcementId;
	
	private Integer likes;
	
	private Integer dislikes;

	public VoteResult() {
	}
	
	public VoteResult(Integer likes, Integer dislikes) {
		this.likes = likes;
		this.dislikes = dislikes;
	}
	
	@Id
	@Column(name = "announcement_id")
	public Integer getAnnouncementId() {
		return this.announcementId;
	}
	
	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}
	
	@Column(name = "likes")
	public Integer getLikes() {
		return this.likes;
	}	
	
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	
	@Column(name = "dislikes")
	public Integer getDislikes() {
		return this.dislikes;
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
		VoteResult voteResult = (VoteResult) object;
		return Objects.equals(this.announcementId, voteResult.getAnnouncementId())
				&& Objects.equals(this.likes, voteResult.getLikes())
				&& Objects.equals(this.dislikes, voteResult.getDislikes());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.announcementId, this.likes, this.dislikes);
	}

	@Override
	public String toString() {
		return new StringBuilder("VoteResult [announcementId=").append(this.announcementId)
				.append(", likes=").append(this.likes)
				.append(", dislikes=").append(this.dislikes).append("]").toString();
	}
	
}
