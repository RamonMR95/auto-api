package com.ramonmr95.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition (info = 
@Info(
      title = "Cars",
      version = "0.1",
      description = "Cars API",
      license = @License(name = "Apache 2.0"),
      contact = @Contact(name = "Ramón Moñino Rubio", email = "ramonmr16@gmail.com")
  )
)
@ApplicationPath("/")
public class JAXRSConfiguration extends Application {
	
} 
