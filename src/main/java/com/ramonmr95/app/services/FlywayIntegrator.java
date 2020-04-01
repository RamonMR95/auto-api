package com.ramonmr95.app.services;

import java.sql.Connection;
import java.sql.SQLException;

import javax.interceptor.Interceptors;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import com.ramonmr95.app.interceptors.LoggingInterceptor;

/**
 * 
 * Using Hibernate Service Provider Interface (SPI) we register the integrator
 * and get the datasource by reflection so we do not have to specify the db in
 * persistence.xml
 * 
 * @author Ramón Moñino Rubio
 *
 */
@Interceptors(LoggingInterceptor.class)
public class FlywayIntegrator implements Integrator {

//    private static final String SCRIPT_LOCATIONS = "db/migration";

	/**
	 * 
	 * Gets the datasource and creates a new instance of flyway to set the
	 * datasource and scripts locations used in the migration
	 * 
	 * @param metadata                      Represents the ORM model as determined
	 *                                      from all provided mapping sources
	 * @param sessionFactoryImplementor     Defines the internal contract between
	 *                                      the SessionFactory and other parts
	 *                                      ofHibernate such as implementors of
	 *                                      Type.
	 * @param sessionFactoryServiceRegistry Specialized
	 *                                      {@link org.hibernate.service.ServiceRegistry}
	 *                                      implementation that holds services which
	 *                                      need access to the
	 *                                      {@link org.hibernate.SessionFactory}
	 *                                      during initialization
	 */
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor,
			SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		Connection connection = null;
		try {
			DataSource dataSource = (DataSource) sessionFactoryImplementor.getProperties()
					.get("hibernate.connection.datasource");

			connection = dataSource.getConnection();

			Flyway flyway = new Flyway();
//            flyway.setLocations(SCRIPT_LOCATIONS);
			flyway.setDataSource(dataSource);
			flyway.setBaselineOnMigrate(true);
			flyway.migrate();
		} catch (SQLException e) {
			throw new RuntimeException("Unable to create JDBC connection.", e);
		} finally {
			if (connection != null) {
				JdbcUtils.closeConnection(connection);
			}
		}
	}

	public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,
			SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		// Not used
	}

}
