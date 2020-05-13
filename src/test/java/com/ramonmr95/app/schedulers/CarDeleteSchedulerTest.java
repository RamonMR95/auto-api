package com.ramonmr95.app.schedulers;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Properties;

import javax.ejb.TimerService;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ramonmr95.app.exceptions.EntityNotFoundException;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;
import com.ramonmr95.app.services.CarService;

@RunWith(MockitoJUnitRunner.class)
public class CarDeleteSchedulerTest {

	@InjectMocks
	private CarDeleteScheduler carDeleteScheduler;

	@Mock
	private TimerService timerService;

	@Mock
	private Properties properties;

	@Mock
	private CarService carService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	// Valid values
	@Test
	public void WhenDeletingCars_ShouldBeExecutedOnce() {
		try {
			Mockito.when(this.carService.getCarsWithDeleteFlag()).thenReturn(new ArrayList<>());
			Mockito.doNothing().when(this.carService).deleteCar(Mockito.any(String.class));
			this.carDeleteScheduler.deleteCars();
			Mockito.verify(this.carService, Mockito.times(1)).getCarsWithDeleteFlag();
		} catch (EntityNotFoundException | InvalidUUIDFormatException e) {
			fail("Should not get here");
		}

	}

}
