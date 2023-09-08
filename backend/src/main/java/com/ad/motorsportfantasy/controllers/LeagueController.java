package com.ad.motorsportfantasy.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ad.motorsportfantasy.authentication.UserRepository;
import com.ad.motorsportfantasy.dtos.LeagueDTO;
import com.ad.motorsportfantasy.dtos.StandingsDTO;
import com.ad.motorsportfantasy.exceptions.AccessDeniedException;
import com.ad.motorsportfantasy.exceptions.FieldNotValidException;
import com.ad.motorsportfantasy.exceptions.LeagueAlreadyExistsException;
import com.ad.motorsportfantasy.exceptions.LeagueNotFoundException;
import com.ad.motorsportfantasy.exceptions.SeriesNotFoundException;
import com.ad.motorsportfantasy.exceptions.UserNotFoundException;
import com.ad.motorsportfantasy.model.League;
import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.model.Series;
import com.ad.motorsportfantasy.model.Standings;
import com.ad.motorsportfantasy.model.User;
import com.ad.motorsportfantasy.repos.LeagueRepository;
import com.ad.motorsportfantasy.repos.PlayerRepository;
import com.ad.motorsportfantasy.repos.SeriesRepository;

@RestController
public class LeagueController {
	private UserRepository userRepository;
	private LeagueRepository leagueRepository;
	private PlayerRepository playerRepository;
	private SeriesRepository seriesRepository;

	LeagueController(UserRepository userRepository, LeagueRepository leagueRepository, PlayerRepository playerRepository, SeriesRepository seriesRepository) {
		this.userRepository = userRepository;
		this.leagueRepository = leagueRepository;
		this.playerRepository = playerRepository;
		this.seriesRepository = seriesRepository;
	}

	private User get_current_user() throws UserNotFoundException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = (String) principal;
		}

		return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	@PostMapping("/leagues")
	public ResponseEntity<?> create_league(@RequestBody String leagueName)
		throws FieldNotValidException, LeagueNotFoundException, UserNotFoundException, LeagueAlreadyExistsException {
			
		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("Name not valid");
		}

		User user = get_current_user();

		League league = new League(leagueName, user);
		//TODO: refer to league with name#id for better uniqueness
		try {
			leagueRepository.save(league);
		} catch (DataIntegrityViolationException e) {
			throw new LeagueAlreadyExistsException("Another league with this name already exists");
		}
		join_league(leagueName);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/leagues")
	public ResponseEntity<?> get_leagues() throws FieldNotValidException, UserNotFoundException {
		User user = get_current_user();

		List<LeagueDTO> ls = new ArrayList<>();

		for(Player p : user.getPlayers()) {
			ls.add(LeagueDTO.fromLeague(p.getLeague()));
		}

		return ResponseEntity.ok(ls);
	}

	@PostMapping("/leagues/{league}/participants")
	public ResponseEntity<?> join_league(@PathVariable("league") String leagueName)
		throws FieldNotValidException, LeagueNotFoundException, UserNotFoundException {

		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("Name not valid");
		}

		League league = leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException("League not found"));
		User user = get_current_user();

		Player player = new Player(user, league);

		user.getPlayers().add(player);
		league.getParticipants().add(player);

		leagueRepository.save(league);
		playerRepository.save(player);
		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/leagues/{league}/participants")
	private ResponseEntity<?> delete_participant(@PathVariable("league") String leagueName)
		throws FieldNotValidException, LeagueNotFoundException, UserNotFoundException {

		if (leagueName == null || leagueName.isBlank() || leagueName == "Global League") {
			throw new FieldNotValidException("League not valid");
		}

		League league = leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException("League not found"));
		User user = get_current_user();

		if (user.equals(league.getOwner())) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Owner cannot leave the league");
		}
		
		Player player = null;
		for (Player p : league.getParticipants()) {
			if (p.getUser().getUsername().equals(user.getUsername())) {
				player = p;
				break;
			}
		}

		if (player == null) {
			throw new UserNotFoundException("Player not found");
		}

		playerRepository.delete(player);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/leagues/{league}/series")
	public ResponseEntity<?> addLeagueSeries(@PathVariable("league") String leagueName, @RequestBody String seriesName)
		throws FieldNotValidException, LeagueNotFoundException, AccessDeniedException, UserNotFoundException, SeriesNotFoundException {
		
		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("League name not valid");
		}

		if (seriesName == null || seriesName.isBlank()) {
			throw new FieldNotValidException("Series name not valid");
		}

		League league = leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException("League not found"));
		User user = get_current_user();

		if (!user.equals(league.getOwner())) {
			throw new AccessDeniedException("Only the owner can modify the league");
		}

		Series series = seriesRepository.findByName(seriesName).orElseThrow(() -> new SeriesNotFoundException("Series not found"));

		if (!league.getSeries().contains(series)) {
			league.getSeries().add(series);
			leagueRepository.save(league);
		}

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/leagues/{league}/series/{series}")
	public ResponseEntity<?> removeLeagueSeries(@PathVariable("league") String leagueName, @PathVariable("series") String seriesName)
		throws FieldNotValidException, LeagueNotFoundException, AccessDeniedException, UserNotFoundException, SeriesNotFoundException {
		
		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("League name not valid");
		}

		if (seriesName == null || seriesName.isBlank()) {
			throw new FieldNotValidException("Series name not valid");
		}

		League league = leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException("League not found"));
		User user = get_current_user();

		if (!user.equals(league.getOwner())) {
			throw new AccessDeniedException("Only the owner can modify the league");
		}

		for (Series series : league.getSeries()) {
			if (series.getName().equals(seriesName)) {
				league.getSeries().remove(series);
				leagueRepository.save(league);
				break;
			}
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/leagues/{league}/standings")
	public ResponseEntity<?> league_standings(@PathVariable("league") String leagueName)
		throws FieldNotValidException, LeagueNotFoundException {

		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("League name not valid");
		}

		League league = leagueRepository.findByName(leagueName).orElseThrow(() -> new LeagueNotFoundException("League not found"));
		Map<String, Set<StandingsDTO> > standings = new HashMap<>();

		for (Series series : league.getSeries()) {
			standings.put(series.getName(), new HashSet<>());
		}

		for (Player player : league.getParticipants()) {
			for (Standings s : player.getStandings()) {
				standings.get(s.getSeries().getName()).add(StandingsDTO.fromStandings(s));
			}
		}

		standings = createMissingStandings(league, standings);

		return ResponseEntity.ok(standings);
	}

	private Map<String, Set<StandingsDTO> > createMissingStandings(League league, Map<String, Set<StandingsDTO> > standings) {
		for (Player p : league.getParticipants()) {
			for (String series : standings.keySet()) {
				
				boolean found = false;
				for (StandingsDTO s : standings.get(series)) {
					if (s.getPlayer().equals(p.getUser().getUsername())) {
						found = true;
						break;
					}
				}

				if (!found) {
					standings.get(series).add(new StandingsDTO(series, p.getUser().getUsername(), 0));
				}
			}
		}

		return standings;
	}
}
