package com.tide.interview.persistence.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Vote domain object.
 */
@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "announcement_id" }))
public class Vote implements java.io.Serializable {

	private Integer id;
	private Announcement announcement;
	private User user;
	private boolean positive;

	public Vote() {
	}

	public Vote(Integer id, Announcement announcement, User user, boolean positive) {
		this.id = id;
		this.announcement = announcement;
		this.user = user;
		this.positive = positive;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "announcement_id", nullable = false)
	public Announcement getAnnouncement() {
		return this.announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "positive", nullable = false)
	public boolean isPositive() {
		return this.positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		Vote vote = (Vote) object;
		return Objects.equals(this.id, vote.getId())
				&& Objects.equals(this.announcement, vote.getAnnouncement())
				&& Objects.equals(this.user, vote.getUser())
				&& Objects.equals(this.positive, vote.isPositive());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.announcement, this.user, this.positive);
	}

	@Override
	public String toString() {
		return new StringBuilder("Vote [id=").append(id)
				.append(", announcement=").append(this.announcement)
				.append(", user=").append(this.user)
				.append(", positive=").append(this.positive).append("]").toString();
	}

}
