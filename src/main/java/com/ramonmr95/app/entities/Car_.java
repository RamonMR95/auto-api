package com.ramonmr95.app.entities;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Car.class)
public class Car_ {

	public static volatile SingularAttribute<Car, UUID> id;
	public static volatile SingularAttribute<Car, Brand> brand;
	public static volatile SingularAttribute<Car, String> model;
	public static volatile SingularAttribute<Car, String> color;
	public static volatile SingularAttribute<Car, Date> registration;
	public static volatile SingularAttribute<Car, Country> country;
	public static volatile SetAttribute<Car, String> carComponents;
	public static volatile SingularAttribute<Car, Date> created_at;
	public static volatile SingularAttribute<Car, Date> updated_at;

	public static final String ID = "id";
	public static final String BRAND = "brand";
	public static final String MODEL = "model";
	public static final String COLOR = "color";
	public static final String REGISTRATION = "registration";
	public static final String CARCOMPONENTS = "carComponents";
	public static final String CREATED_AT = "created_at";
	public static final String UPDATED_AT = "updated_at";
}
