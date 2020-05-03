package com.ramonmr95.app.migrations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Brand;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.entities.Country;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.exceptions.NotUniqueKeyException;
import com.ramonmr95.app.interceptors.LoggingInterceptor;
import com.ramonmr95.app.services.BrandService;
import com.ramonmr95.app.services.CarService;
import com.ramonmr95.app.services.CountryService;

@Startup
@Singleton
@Interceptors(LoggingInterceptor.class)
public class DBMigration {

	private final Logger log = LogManager.getLogger(DBMigration.class);

	@EJB
	private CarService carService;

	@EJB
	private BrandService brandService;

	@EJB
	private CountryService countryService;

	@PostConstruct
	public void setup() {
		Brand bmw = new Brand("BMW");
		Brand ford = new Brand("Ford");
		Brand seat = new Brand("Seat");

		Country germany = new Country("Germany", "DEU", "");
		Country spain = new Country("Spain", "ESP", "");
		Country usa = new Country("United states", "USA", "");

		try {
			this.brandService.createBrand(bmw);
			this.brandService.createBrand(ford);
			this.brandService.createBrand(seat);
		} catch (EntityValidationException | NotUniqueKeyException e) {
			log.error(e.getMessage());
		}

		try {
			this.countryService.createCountry(germany);
			this.countryService.createCountry(spain);
			this.countryService.createCountry(usa);
		} catch (EntityValidationException | NotUniqueKeyException e) {
			log.error(e.getMessage());
		}

		try {

			Car car1 = new Car(bmw, "Serie 3", "black", germany, new Timestamp(new Date().getTime()));
			car1.addComponent("Steering wheel");
			Car car2 = new Car(ford, "focus", "white", usa, new Timestamp(new Date().getTime()));
			car2.addComponent("leather seats");
			Car car3 = new Car(seat, "cordoba", "green", spain, new Timestamp(new Date().getTime()));
			car3.addComponent("xenon lights");
			List<Car> cars = Arrays.asList(car1, car2, car3);

			for (Car car : cars)
				this.carService.createCar(car.getDto());

		} catch (EntityValidationException | EntityNotFoundException | InvalidUUIDFormatException e) {
			log.error(e.getMessage());
		}

	}

}
