package com.ad.motorsportfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.Series;
import java.util.Optional;


@RepositoryRestResource
public interface SeriesRepository extends JpaRepository<Series, Long> {
	Optional<Series> findByName(String name);
}
