package com.ad.motorsportfantasy.controllers;

import java.util.Comparator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.motorsportfantasy.authentication.UserRepository;
import com.ad.motorsportfantasy.exceptions.ContractAlreadyExistsException;
import com.ad.motorsportfantasy.exceptions.DriverNotFoundException;
import com.ad.motorsportfantasy.exceptions.FieldNotValidException;
import com.ad.motorsportfantasy.exceptions.LeagueNotFoundException;
import com.ad.motorsportfantasy.exceptions.NotEnoughMoneyException;
import com.ad.motorsportfantasy.exceptions.SeriesNotFoundException;
import com.ad.motorsportfantasy.exceptions.UserNotFoundException;
import com.ad.motorsportfantasy.model.Contract;
import com.ad.motorsportfantasy.model.Driver;
import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.model.Result;
import com.ad.motorsportfantasy.model.Series;
import com.ad.motorsportfantasy.model.User;
import com.ad.motorsportfantasy.repos.ContractRepository;
import com.ad.motorsportfantasy.repos.DriverRepository;
import com.ad.motorsportfantasy.repos.PlayerRepository;

@RestController
public class DriverController {

	private UserRepository userRepository;
	private PlayerRepository playerRepository;
	private DriverRepository driverRepository;
	private ContractRepository contractRepository;

	DriverController(UserRepository userRepository, DriverRepository driverRepository,
		PlayerRepository playerRepository, ContractRepository contractRepository) {

		this.userRepository = userRepository;
		this.driverRepository = driverRepository;
		this.playerRepository = playerRepository;
		this.contractRepository = contractRepository;
	}

	private User get_current_user() throws UserNotFoundException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = (String) principal;
		}

		return userRepository.findByUsername(username)
			.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	@PostMapping("leagues/{league}/series/{series}/drivers/{driver}/contracts")
	public ResponseEntity<?> create_contract(@PathVariable("league") String leagueName,
		@PathVariable("series") String seriesName, @PathVariable("driver") String driverName)
		throws UserNotFoundException, LeagueNotFoundException, SeriesNotFoundException, DriverNotFoundException,
		FieldNotValidException, ContractAlreadyExistsException, NotEnoughMoneyException {
		
		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("League name not valid");
		}
		
		if (seriesName == null || seriesName.isBlank()) {
			throw new FieldNotValidException("Series name not valid");
		}
		
		if (driverName == null || driverName.isBlank()) {
			throw new FieldNotValidException("Driver name not valid");
		}

		User user = get_current_user();

		//TODO: limit number of contracts per series

		Player player = null;
		for (Player p : user.getPlayers()) {
			if (p.getLeague().getName().equals(leagueName)) {
				player = p;
				break;
			}
		}
		if (player == null) {
			throw new LeagueNotFoundException("League not found in " + user.getUsername() + "'s leagues");
		}
		for (Contract c : player.getContracts()) {
			if (c.getDriver().getFullname().equals(driverName)) {
				throw new ContractAlreadyExistsException("Contract already made");
			}
		}
		
		Series series = null;
		for (Series s : player.getLeague().getSeries()) {
			if (s.getName().equals(seriesName)) {
				series = s;
				break;
			}
		}
		if (series == null) {
			throw new SeriesNotFoundException("Series not found");
		}

		Driver driver = null;
		for (Driver d : series.getDrivers()) {
			if (d.getFullname().equals(driverName)) {
				driver = d;
				break;
			}
		}
		if (driver == null) {
			throw new DriverNotFoundException("Driver not found");
		}

		if (driver.getValue() > player.getMoney()) {
			throw new NotEnoughMoneyException("Not enough money");
		}

		int start_event = 0;
		start_event = driver.getResults().stream().max(Comparator.comparing(Result::getIndex)).get().getIndex();

		Contract contract = new Contract(player, driver, start_event, driver.getValue(), driver.getPoints(), driver.getPoints(), 0.0f);
		driver.getContracts().add(contract);
		player.getContracts().add(contract);
		player.setMoney(player.getMoney() - driver.getValue());

		contractRepository.save(contract);
		driverRepository.save(driver);
		playerRepository.save(player);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("leagues/{league}/series/{series}/drivers/{driver}/contracts")
	public ResponseEntity<?> delete_contract(@PathVariable("league") String leagueName,
		@PathVariable("series") String seriesName, @PathVariable("driver") String driverName)
		throws LeagueNotFoundException, UserNotFoundException, FieldNotValidException {
		
		if (leagueName == null || leagueName.isBlank()) {
			throw new FieldNotValidException("League name not valid");
		}
		
		if (seriesName == null || seriesName.isBlank()) {
			throw new FieldNotValidException("Series name not valid");
		}
		
		if (driverName == null || driverName.isBlank()) {
			throw new FieldNotValidException("Driver name not valid");
		}

		User user = get_current_user();

		Player player = null;
		for (Player p : user.getPlayers()) {
			if (p.getLeague().getName().equals(leagueName)) {
				player = p;
			}
		}
		if (player == null) {
			throw new LeagueNotFoundException("League not found in " + user.getUsername() + "'s leagues");
		}

		Contract contract = null;
		for (Contract c : player.getContracts()) {
			if (c.getDriver().getFullname().equals(driverName) && c.getDriver().getSeries().getName().equals(seriesName)) {
				contract = c;
				break;
			}
		}

		if (contract == null) {
			return ResponseEntity.ok().build();
		}

		player.setMoney(player.getMoney() + contract.getDriver().getValue());

		playerRepository.save(player);
		contractRepository.delete(contract);

		return ResponseEntity.ok().build();
	}
}