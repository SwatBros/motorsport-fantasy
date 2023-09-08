package com.ad.motorsportfantasy.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.Driver;
import com.ad.motorsportfantasy.model.Result;

@RepositoryRestResource
public interface ResultRepository extends JpaRepository<Result, Long> {
	public Optional<Result> findByDriverAndIndex(Driver driver, int index);
}
