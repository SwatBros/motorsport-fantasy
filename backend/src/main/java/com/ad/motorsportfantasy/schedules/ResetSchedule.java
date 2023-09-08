package com.ad.motorsportfantasy.schedules;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.repos.ContractRepository;
import com.ad.motorsportfantasy.repos.DriverRepository;
import com.ad.motorsportfantasy.repos.PlayerRepository;
import com.ad.motorsportfantasy.repos.ResultRepository;
import com.ad.motorsportfantasy.repos.StandingsRepository;

import jakarta.transaction.Transactional;

@Service
public class ResetSchedule {
	private ContractRepository contractRepository;
	private DriverRepository driverRepository;
	private ResultRepository resultRepository;
	private StandingsRepository standingsRepository;
	private PlayerRepository playerRepository;

	ResetSchedule(ContractRepository contractRepository, DriverRepository driverRepository,
		ResultRepository resultRepository, StandingsRepository standingsRepository, PlayerRepository playerRepository) {

		this.contractRepository = contractRepository;
		this.driverRepository = driverRepository;
		this.resultRepository = resultRepository;
		this.standingsRepository = standingsRepository;
		this.playerRepository = playerRepository;
	}

	@Scheduled(cron = "0 0 0 1 1 *")
	@Transactional
	public void resetGame() {
		contractRepository.deleteAll();
		driverRepository.deleteAll();
		resultRepository.deleteAll();
		standingsRepository.deleteAll();

		List<Player> players = playerRepository.findAll();

		for (Player p : players) {
			p.setPoints(0);
			p.setMoney(40000);

			playerRepository.save(p);
		}
	}
}
