package com.ad.motorsportfantasy.authentication;

import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ad.motorsportfantasy.exceptions.FieldNotValidException;
import com.ad.motorsportfantasy.exceptions.LeagueNotFoundException;
import com.ad.motorsportfantasy.exceptions.UserAlreadyExistsException;
import com.ad.motorsportfantasy.exceptions.UserNotFoundException;
import com.ad.motorsportfantasy.model.League;
import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.model.User;
import com.ad.motorsportfantasy.repos.LeagueRepository;
import com.ad.motorsportfantasy.repos.PlayerRepository;

import jakarta.servlet.ServletException;

@RestController
public class AuthenticationController {
	private UserRepository userRepository;
	private LeagueRepository leagueRepository;
	private PlayerRepository playerRepository;

	AuthenticationController(UserRepository userRepository, LeagueRepository leagueRepository, PlayerRepository playerRepository) {
		this.userRepository = userRepository;
		this.leagueRepository = leagueRepository;
		this.playerRepository = playerRepository;
	}

	@PostMapping("/process_register")
	public ResponseEntity<?> process_register(@RequestBody User user) throws FieldNotValidException, UserAlreadyExistsException, LeagueNotFoundException, UserNotFoundException, ServletException {
		if (user.getUsername() == null || user.getUsername().isBlank()) {
			throw new FieldNotValidException("Username is not valid");
		}

		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new FieldNotValidException("Password is not valid");
		}

		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("User already exists");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		User createdUser = userRepository.save(user);

		try {
			//TODO: create the global league if it doesn't exist?
			League league = leagueRepository.findByName("Global League").orElseThrow(() -> new LeagueNotFoundException("Something went wrong"));
			Player player = new Player(user, league);
			if (createdUser.getPlayers() == null) {
				createdUser.setPlayers(new HashSet<Player>());
			}
			createdUser.getPlayers().add(player);
			if (league.getParticipants() == null) {
				league.setParticipants(new HashSet<Player>());
			}
			league.getParticipants().add(player);

			createdUser = userRepository.save(createdUser);
			leagueRepository.save(league);
			playerRepository.save(player);
		} catch (Exception e) {
			userRepository.delete(createdUser);
			throw e;
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/process_login")
	public void process_login() {
	}
}
