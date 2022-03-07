package com.nwahs.backend;

import com.nwahs.backend.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	/**
	 * Allows your backend dashboard to do
	 * HTTP requests to your MySQL database;
	 * prevents CORS-related problems
	 */
	@Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings( CorsRegistry registry ) {
				// Add CORS mapping to every
				// HTTP request; only allowing
				// specific URL address
				registry.addMapping( "/**" )
						.allowedOrigins( Strings.svelteBackend );
			}
		};
	}

}
