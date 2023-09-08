package com.ad.motorsportfantasy.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.League;

@RepositoryRestResource()
public interface LeagueRepository extends JpaRepository<League, Long> {
	public Optional<League> findByName(String name);
}
