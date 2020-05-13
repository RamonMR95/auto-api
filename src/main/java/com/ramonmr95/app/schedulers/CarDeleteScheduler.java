package com.ramonmr95.app.schedulers;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.services.CarService;

/**
 * 
 * Programmatic timer that will delete the marked cars with delete: true flag.
 * Uses a cron expression.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Startup
@Singleton
public class CarDeleteScheduler {

	private static final Logger log = LogManager.getLogger(CarDeleteScheduler.class);

	@EJB
	private CarService carService;

	@Resource
	private TimerService timerService;

	private ScheduleExpression scheduleExpression;

	private Properties properties;

	@PostConstruct
	public void schedule() {
		this.properties = new Properties();
		String cronExp = this.getCronExpressionFromPropertiesFile();
		this.setScheduleExpression(cronExp);
		this.setTimer();
	}

	/**
	 * 
	 * Deletes the marked cars after every timeout.
	 */
	@Timeout
	public void deleteCars() {
		try {
			List<Car> cars = this.carService.getCarsWithDeleteFlag();
			for (Car car : cars) {
				this.carService.deleteCar(car.getId().toString());
				log.info(String.format("Deleted car with id: %s", car.getId()));
			}
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			log.error("Error deleting cars");
		}
	}

	/**
	 * 
	 * Gets the cron expression from the properties file.
	 * 
	 * @return cronexpression Cron expression as a string.
	 */
	public String getCronExpressionFromPropertiesFile() {
		String exp = null;
		try {
			properties
					.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
			exp = properties.getProperty("cars.deletion.cron.expression");
		} catch (IOException e) {
		}
		return exp;
	}

	/**
	 * 
	 * Sets the schedule expression used by the timer given a cron expression.
	 * 
	 * @param cronExp Cron expression as a string
	 */
	public void setScheduleExpression(String cronExp) {
		this.scheduleExpression = new ScheduleExpression();
		scheduleExpression.second(cronExp.split(" ")[0]);
		scheduleExpression.minute(cronExp.split(" ")[1]);
		scheduleExpression.hour(cronExp.split(" ")[2]);
		scheduleExpression.dayOfMonth(cronExp.split(" ")[3]);
		scheduleExpression.month(cronExp.split(" ")[4]);
		scheduleExpression.dayOfWeek(cronExp.split(" ")[5]);
	}

	/**
	 * 
	 * Sets the timer with an scheduleexpresion.
	 */
	public void setTimer() {
		TimerConfig timer = new TimerConfig();
		timer.setInfo("Every 15 mins");
		this.timerService.createCalendarTimer(scheduleExpression, timer);
	}

}
