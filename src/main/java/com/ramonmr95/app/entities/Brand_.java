package com.ramonmr95.app.entities;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Brand.class)
public class Brand_ {

	public static volatile SingularAttribute<Brand, UUID> id;
	public static volatile SingularAttribute<Brand, String> name;
	public static volatile SingularAttribute<Brand, Date> created_at;
	public static volatile SingularAttribute<Brand, Date> updated_at;

	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String CREATED_AT = "created_at";
	public static final String UPDATED_AT = "updated_at";
}
