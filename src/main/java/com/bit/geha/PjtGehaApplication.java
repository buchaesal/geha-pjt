package com.bit.geha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
public class PjtGehaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PjtGehaApplication.class, args);
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.addTemplateResolver(new UrlTemplateResolver());
	    templateEngine.addDialect(new LayoutDialect());
	    return templateEngine;
	}
}

