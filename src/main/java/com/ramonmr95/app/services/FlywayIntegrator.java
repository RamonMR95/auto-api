package com.ramonmr95.app.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.util.jdbc.JdbcUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class FlywayIntegrator implements Integrator {

//    private static final String SCRIPT_LOCATIONS = "db/migration";
    private static final Logger log = LogManager.getLogger(FlywayIntegrator.class);

    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    	log.info("Entering integrate!");
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) sessionFactoryImplementor
                    .getProperties()
                    .get("hibernate.connection.datasource" );

            connection = dataSource.getConnection();

            Flyway flyway = new Flyway();
//            flyway.setLocations(SCRIPT_LOCATIONS);
            flyway.setDataSource(dataSource);
            flyway.setBaselineOnMigrate(true);
            flyway.migrate();

            log.info("Flyway migration finished. Database version is " + flyway.info().current().getVersion());
        }
        catch (SQLException e) {
            throw new RuntimeException("Unable to create JDBC connection.", e);
        }
        finally {
            if (connection != null) {
                JdbcUtils.closeConnection(connection);
            }
        }
    }

    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    	// Not used
    }

}