package com.ad.motorsportfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.Standings;

@RepositoryRestResource
public interface StandingsRepository extends JpaRepository<Standings, Long> {
	
}
