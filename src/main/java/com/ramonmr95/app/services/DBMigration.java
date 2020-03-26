package com.ramonmr95.app.services;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.utils.EntityValidationException;

@Startup
@Singleton
public class DBMigration {
	
	private static final Logger log = LogManager.getLogger(DBMigration.class);
	
	@EJB
	private CarService carService;
	
	@PostConstruct
	public void setup() {
		log.info("Entering setup");
		List<Car> cars = Arrays.asList(new Car("BMW", "Germany", new Timestamp(new Date().getTime())),
				new Car("Mercedes", "Germany", new Timestamp(new Date().getTime())), 
				new Car("Seat", "Spain", new Timestamp(new Date().getTime())));
		
		try {
			for (Car car : cars) 
				this.carService.createCar(car);
		} 
		catch (EntityValidationException e) {}
		log.info("Exiting setup");
	}

}
