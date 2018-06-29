package org.pavelf.nevada.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class Application {

	/**
	 * Describes first letter of application specific Accept header. 
	 * For example application/vnd.yourapp whereas full header is
	 * application/vnd.yourapp.domain+json;version=1.0
	 * */
	public static final String APPLICATION_ACCEPT_PREFIX = 
			"application/vnd.nevada";
	
	
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
