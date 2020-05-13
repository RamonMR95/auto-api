package com.ramonmr95.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ramonmr95.app.resources.BrandResourceTest;
import com.ramonmr95.app.resources.CarResourceAsyncTest;
import com.ramonmr95.app.resources.CarResourceRestTest;
import com.ramonmr95.app.resources.CountryResourceTest;
import com.ramonmr95.app.services.BrandServiceTest;
import com.ramonmr95.app.services.CarServiceTest;
import com.ramonmr95.app.services.CountryServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ CarResourceRestTest.class, CarResourceAsyncTest.class, CountryResourceTest.class, BrandResourceTest.class, CarServiceTest.class,
		CountryServiceTest.class, BrandServiceTest.class })
public class AppTest {

}
