package org.pavelf.nevada.api;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.pavelf.nevada.api.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Application {

	/**
	 * Describes first letter of application specific Accept header. 
	 * For example application/vnd.yourapp whereas full header is
	 * application/vnd.yourapp.domain+json;version=1.0
	 * */
	public static final String APPLICATION_ACCEPT_PREFIX = "application/vnd.nevada";
	
	@Autowired
	public Application(DataSource dataSource, Environment env) {
		
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		/*Properties defaultProperties = new Properties();
		defaultProperties.put("debug", "true");
		defaultProperties.put("logging.file", System.getProperty("user.dir") + "/logging/log.txt");
		defaultProperties.put("logging.pattern.dateformat", "yyyy-MM-dd HH:mm:ss.SSS");
		defaultProperties.put("debug", "true");
		defaultProperties.put("debug", "true");
		app.setDefaultProperties(defaultProperties);*/
		app.run(args);
		//SpringApplication.run(Application.class, args);

	}
	

}
