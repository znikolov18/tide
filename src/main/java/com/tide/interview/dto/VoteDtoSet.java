package com.tide.interview.dto;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Set of vote data transfer objects.
 */
public class VoteDtoSet {

	private Set<VoteDto> votes = Sets.newHashSet();
	
	public VoteDtoSet() {
	}

	public VoteDtoSet(Set<VoteDto> votes) {
		this.votes = votes;
	}
	
	public Set<VoteDto> getVotes() {
		return votes;
	}

	public void setVotes(Set<VoteDto> votes) {
		this.votes = votes;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		VoteDtoSet voteSetDto = (VoteDtoSet) object;
		return Objects.equals(this.votes, voteSetDto.getVotes());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.votes);
	}

	@Override
	public String toString() {
		return new StringBuilder("VoteSetDto [voteDtos=").append(votes).append("]").toString();
	}
	
}
