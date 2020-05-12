package com.ramonmr95.app.listeners;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ramonmr95.app.dtos.CarDto;
import com.ramonmr95.app.entities.Car;
import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.EntityValidationException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.services.CarService;

/**
 * 
 * Defines the JMS interface used to create, modify and remove cars
 * asynchronously.
 * 
 * @author Ramón Moñino Rubio
 *
 */
@MessageDriven(mappedName = "jms/Queue", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class JmsMessageListener implements MessageListener {

	private static Logger log = LogManager.getLogger(JmsMessageListener.class);

	@EJB
	private CarService carService;

	public JmsMessageListener() {

	}

	/**
	 * 
	 * Depending on the method name given it will perform the create, update, delete
	 * operation with car service.
	 */
	@Override
	public void onMessage(Message message) {
		try {
			String method = message.getStringProperty("METHOD");
			String id = message.getStringProperty("id");
			String jsonString = message.getBody(String.class);
			CarDto carDto = null;
			if (jsonString != null) {
				carDto = CarDto.getCarDtoFromJsonString(jsonString);
			}

			switch (method.toUpperCase()) {
			case "PUT":
				if (id != null && carDto != null) {
					this.carService.updateCar(carDto, id);
					log.info(String.format("Updated car with id: %s", id));
				} else {
					log.error("Both car and car id are required.");
				}
				break;
			case "DELETE":
				if (id != null) {
					this.carService.deleteCar(id);
					log.info(String.format("Deleted car with id: %s", id));
				} else {
					log.error(String.format("Cannot find any car with id: %s", id));
				}
				break;
			case "POST":
				if (carDto != null) {
					Car car = this.carService.createCar(carDto);
					log.info(String.format("Created car with id: %s", car.getId()));
				} else {
					log.error("The car is required.");
				}
				break;
			default:
				log.error("Invalid http METHOD.");
				break;
			}

		} catch (JMSException | EntityNotFoundException | EntityValidationException | InvalidUUIDFormatException e) {
			log.error(String.format("JMSException: %s", e.getMessage()));
		}

	}

}
