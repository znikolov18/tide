package com.tide.interview.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Announcement data transfer object.
 */
public class AnnouncementDto {

	private Integer id;
	
	@NotNull
    @Size(min=2, max=100)
	private String subject;
	
	@NotNull
    @Size(min=2, max=500)
	private String summary;

	public AnnouncementDto() {
	}
	
	public AnnouncementDto(String subject, String summary) {
		this.subject = subject;
		this.summary = summary;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSummary() {
		return summary;
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
		AnnouncementDto announcementDto = (AnnouncementDto) object;
		return Objects.equals(this.id, announcementDto.getId())
				&& Objects.equals(this.subject, announcementDto.getSubject())
				&& Objects.equals(this.summary, announcementDto.getSummary());
	}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.subject, this.summary);
    }

	@Override
	public String toString() {
		return new StringBuilder("AnnouncementDto [id=").append(id)
				.append(", subject=").append(subject)
				.append(", summary=").append(summary).append("]").toString();
	}
    
}
