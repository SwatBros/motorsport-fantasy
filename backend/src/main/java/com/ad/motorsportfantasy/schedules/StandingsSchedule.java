package com.ad.motorsportfantasy.schedules;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ad.motorsportfantasy.exceptions.FieldNotValidException;
import com.ad.motorsportfantasy.model.Contract;
import com.ad.motorsportfantasy.model.Driver;
import com.ad.motorsportfantasy.model.Player;
import com.ad.motorsportfantasy.model.Result;
import com.ad.motorsportfantasy.model.Series;
import com.ad.motorsportfantasy.model.Standings;
import com.ad.motorsportfantasy.repos.ContractRepository;
import com.ad.motorsportfantasy.repos.DriverRepository;
import com.ad.motorsportfantasy.repos.PlayerRepository;
import com.ad.motorsportfantasy.repos.ResultRepository;
import com.ad.motorsportfantasy.repos.SeriesRepository;
import com.ad.motorsportfantasy.repos.StandingsRepository;

@Service
public class StandingsSchedule {
	private SeriesRepository seriesRepository;
	private final WebClient webClient;
	private DriverRepository driverRepository;
	private ContractRepository contractRepository;
	private PlayerRepository playerRepository;
	private StandingsRepository standingsRepository;
	private ResultRepository resultRepository;
	
	StandingsSchedule(SeriesRepository seriesRepository, DriverRepository driverRepository,
		ContractRepository contractRepository, PlayerRepository playerRepository,
		StandingsRepository standingsRepository, ResultRepository resultRepository) {

		this.seriesRepository = seriesRepository;
		this.driverRepository = driverRepository;
		this.contractRepository = contractRepository;
		this.playerRepository = playerRepository;
		this.standingsRepository = standingsRepository;
		this.resultRepository = resultRepository;

		this.webClient = WebClient.create();
	}

	@Scheduled( /*cron = "0 0 0 * * MON"*/  fixedDelay = 1000000, initialDelay = 100)
	@Transactional
	public void updateStandings() {
		for (Series s : seriesRepository.findAll()) {
			try {
				updateSeries(s);
			} catch (FieldNotValidException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateSeries(Series series) throws FieldNotValidException, JSONException {
		if (series.getApiRef() == null || series.getApiRef().isBlank()) {
			throw new FieldNotValidException("Series " + series.getName() + ": Invalid API ref: " + series.getApiRef());
		}

		String apiRef = series.getApiRef();
		apiRef.replace("2023", Year.now().getValue() + "");
		String res = webClient.get().uri(apiRef).retrieve().bodyToMono(String.class).block();

		JSONObject jsonString = new JSONObject(res);
		JSONArray standings = jsonString.getJSONArray("standings");

		for (int i = 0; i < standings.length(); ++i) {
			updateDriver(series, standings.getJSONObject(i), jsonString.getJSONArray("races"));
		}

		//TODO: max series points per event to better scale the graph
	}

	private void updateDriver(Series series, JSONObject d, JSONArray calendar) throws JSONException {
		Driver driver = getDriver(series, d.getJSONObject("driver").getString("name"));

		driver.setPoints(d.getInt("totalPoints"));
		driver.setTrend(getDriverTrend(d));
		driver.setPosition(d.getInt("position"));
		driver.setImgRef(d.getJSONObject("driver").getString("picture"));
		driver.setValue(getValue(d, driver.getTrend()));
		updateDriverResults(driver, d.getJSONArray("races"), calendar);

		driverRepository.save(driver);
		seriesRepository.save(series);

		for (Contract c: driver.getContracts()) {
			updateContract(c);
		}
	}

	private void updateContract(Contract contract) {
		updatePoints(contract);
		updateTrend(contract);
	}

	private void updatePoints(Contract contract) {
		int lastDelta = contract.getUpdatedPoints() - contract.getPoints();
		contract.setUpdatedPoints(contract.getDriver().getPoints());
		int newDelta = contract.getUpdatedPoints() - contract.getPoints();

		Player player = contract.getContractor();
		player.setPoints(player.getPoints() - lastDelta + newDelta);

		Standings standings = null;
		for (Standings s : player.getStandings()) {
			if (s.getSeries().getName().equals(contract.getDriver().getSeries().getName())) {
				standings = s;
				break;
			}
		}
		if (standings == null) {
			standings = new Standings();
			standings.setPlayer(player);
			standings.setSeries(contract.getDriver().getSeries());
			standings.setPoints(0);
		}
		standings.setPoints(standings.getPoints() - lastDelta + newDelta);

		standingsRepository.save(standings);
		contractRepository.save(contract);
		playerRepository.save(player);
	}

	private void updateTrend(Contract contract) {
		List<Integer> values = new ArrayList<>();

		contract.getDriver().getResults().stream().sorted(Comparator.comparing(Result::getIndex)).forEach(result -> {
			if (result.getIndex() >= contract.getStartEvent()) {
				values.add(result.getPoints());
			}
		});

		contract.setTrend(getTrend(values));
	}

	private Driver getDriver(Series series, String name) {
		for (Driver driver : series.getDrivers()) {
			if (driver.getFullname().equals(name)) {
				return driver;
			}
		}

		Driver driver = new Driver();
		driver.setFullname(name);
		driver.setSeries(series);
		
		series.getDrivers().add(driver);

		Driver newDriver = driverRepository.save(driver);
		seriesRepository.save(series);

		return newDriver;
	}

	private float getDriverTrend(JSONObject d) throws JSONException {
		JSONArray races = d.getJSONArray("races");
		if (races.length() == 0) {
			return 0;
		}
		List<Integer> points = new ArrayList<>();

		for (int i = 0; i < races.length(); ++i) {
			int p = races.getJSONObject(i).getInt("points");
			points.add(p);
		}
		
		return getTrend(points);
	}

	private float getTrend(List<Integer> values) {
		int ex = 0;
		int exx = 0;
		int ey = 0;
		int exy = 0;


		for (int i = 0; i < values.size(); ++i) {
			int points = values.get(i);

			ex += i;
			exx += i * i;
			ey += points;
			exy += i * points;
		}

		float sxx = exx - (ex * ex) / values.size();
		float sxy = exy - ex * ey / values.size();

		if (sxx == 0) {
			return 0;
		}

		return sxy / sxx;
	}

	private int getValue(JSONObject d, float trend) throws JSONException {
		int points = d.getInt("totalPoints");
		
		int races = d.getJSONArray("races").length();
		races = races > 0 ? races : 1;

		double value = 15 + 1.5 * (points / races);
		value -= 5 * (2 / (1 + 0.3 * Math.pow(2.71, 0.1 * trend)));
		return (int) (value * 10e5);
	}

	private void updateDriverResults(Driver driver, JSONArray races, JSONArray calendar) throws JSONException {
		Set<Result> results = driver.getResults();

		for (int i = 0; i < races.length(); ++i) {
			JSONObject race = races.getJSONObject(i);

			int index = race.getInt("raceNumberInSeason");
			int points = race.getInt("points");
			String name = calendar.getJSONObject(index - 1).getJSONObject("event").getString("shortName");

			resultRepository.findByDriverAndIndex(driver, index).ifPresentOrElse(r -> {
				r.setPoints(points);
				r.setName(name);
			}, () -> {
				Result result = new Result();
				result.setPoints(points);
				result.setIndex(index);
				result.setDriver(driver);
				result.setName(name);

				result = resultRepository.save(result);
				results.add(result);
			});
		}
	}
}
