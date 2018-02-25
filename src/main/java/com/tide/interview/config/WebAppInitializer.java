package com.tide.interview.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Web application initializer
 */
public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) {
		String profile = System.getProperty("spring.profile", "dev");
		servletContext.setInitParameter("spring.profiles.active", profile);
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();  
        context.register(ApplicationConfig.class);  
        context.setServletContext(servletContext);    
        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(context));  
        dynamic.addMapping("/");  
        dynamic.setLoadOnStartup(1);
   }

} 
