package com.tide.interview.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Persistence related configuration.
 */
@Configuration
@EnableJpaRepositories("com.tide.interview.persistence.repository")
@EnableTransactionManagement
@PropertySource({ "classpath:persistence.properties" })
public class PersistenceConfig {

	@Autowired
	private Environment environment;

	/**
	 * Creates entity manager factory.
	 * 
	 * @return entity manager factory
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(getDataSource());
		entityManagerFactory.setPackagesToScan("com.tide.interview.persistence.domain");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactory.setJpaProperties(getHibernateProperties());

		return entityManagerFactory;
	}

	/**
	 * Creates data source.
	 * 
	 * @return data source
	 */
	@Bean
	@Profile("!integrationTest")
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getProperty("ds.driverClassName"));
		dataSource.setUrl(environment.getProperty("ds.url"));
		dataSource.setUsername(environment.getProperty("ds.username"));
		dataSource.setPassword(environment.getProperty("ds.password"));

		return dataSource;
	}

	/**
	 * Creates transaction manager.
	 * 
	 * @param entityManagerFactory entity manager factory
	 * @return transaction manager
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);

		return transactionManager;
	}
	
	/**
	 * Creates persistence exception translation post processor.
	 * 
	 * @return persistence exception translation post processor
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor getExceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Creates hibernate specific properties.
	 * 
	 * @return hibernate properties
	 */
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));

		return properties;
	}
}