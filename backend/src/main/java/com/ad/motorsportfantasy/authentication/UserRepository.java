package com.ad.motorsportfantasy.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ad.motorsportfantasy.model.User;

@RepositoryRestResource()
public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUsername(String username);
}
