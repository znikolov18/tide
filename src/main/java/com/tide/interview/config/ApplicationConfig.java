package com.tide.interview.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 
 * Application configuration.
 *
 */
@Configuration 
@ComponentScan("com.tide.interview") 
@EnableWebMvc  
public class ApplicationConfig {
	
	/**
	 * Creates model mapper.
	 * 
	 * @return model mapper bean.
	 */
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
}
