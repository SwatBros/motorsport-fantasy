package com.ad.motorsportfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.Driver;

@RepositoryRestResource
public interface DriverRepository extends JpaRepository<Driver, Long> {
	
}
