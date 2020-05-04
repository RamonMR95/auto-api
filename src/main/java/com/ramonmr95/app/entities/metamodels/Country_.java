package com.ramonmr95.app.entities.metamodels;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.ramonmr95.app.entities.Country;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Country.class)
public class Country_ {

	public static volatile SingularAttribute<Country, UUID> id;
	public static volatile SingularAttribute<Country, String> name;
	public static volatile SingularAttribute<Country, String> isoCode;
	public static volatile SingularAttribute<Country, String> flagUrl;
	public static volatile SingularAttribute<Country, Date> created_at;
	public static volatile SingularAttribute<Country, Date> updated_at;
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String ISOCODE = "iso_code";
	public static final String FLAGURL = "flag_url";
	public static final String CREATED_AT = "created_at";
	public static final String UPDATED_AT = "updated_at";
}
