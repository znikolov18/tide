package com.tide.interview.persistence.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Announcement domain object.
 */
@Entity
@Table(name = "announcement")
public class Announcement implements java.io.Serializable {

	private Integer id;
	private String subject;
	private String summary;

	public Announcement() {
	}

	public Announcement(Integer id, String subject, String summary) {
		this.id = id;
		this.subject = subject;
		this.summary = summary;
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

	@Column(name = "subject", nullable = false, length = 100)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "summary", length = 500)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		Announcement announcement = (Announcement) object;
		return Objects.equals(this.id, announcement.getId())
				&& Objects.equals(this.subject, announcement.getSubject())
				&& Objects.equals(this.summary, announcement.getSummary());
	}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.subject, this.summary);
    }

	@Override
	public String toString() {
		return new StringBuilder("Announcement [id=").append(this.id)
				.append(", subject=").append(this.subject)
				.append(", summary=").append(this.summary).append("]").toString();
	}

}
