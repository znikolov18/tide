package com.tide.interview.config;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

/**
 * Persistence related configuration for the running of integration tests.
 */
@Configuration
@Profile("integrationTest")
@PropertySource({"classpath:embedded-persistence.properties"})
class EmbeddedPersistenceConfig {

	@Autowired
	private Environment environment;
	
	/**
	 * Creates data source for the in-memory database.
	 * 
	 * @return data source
	 * @throws ManagedProcessException
	 */
    @Bean
    DataSource getDataSource() throws ManagedProcessException {
        DBConfigurationBuilder config = initializeEmbeddedDatabase();

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getProperty("ds.driverClassName"));
		dataSource.setUrl(config.getURL(environment.getProperty("db.name")));
		dataSource.setUsername(environment.getProperty("ds.username"));
		dataSource.setPassword(environment.getProperty("ds.password"));
        
        return dataSource;
    }

    /**
     * Creates in-memory database configuration builder
     * 
     * @return in-memory database configuration builder
     * @throws ManagedProcessException
     */
	private DBConfigurationBuilder initializeEmbeddedDatabase() throws ManagedProcessException {
		DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(0);
       
        DB db = DB.newEmbeddedDB(config.build());
        db.start();
        db.createDB(environment.getProperty("db.name"));
        db.source(environment.getProperty("db.source"));
		return config;
	}
	
}