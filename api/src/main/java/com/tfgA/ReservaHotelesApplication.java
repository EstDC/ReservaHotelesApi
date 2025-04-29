package com.tfgA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
@ComponentScan(basePackages = "com")
@EnableJpaAuditing
@EnableJpaRepositories("com.reservahoteles.infra.repository")
@EntityScan("com.reservahoteles.infra.entity")
@RestController
public class ReservaHotelesApplication {

	@GetMapping("/")
	public RedirectView redirectToSwagger() {
		return new RedirectView("/swagger-ui.html");
	}

	public static void main(String[] args) {
		SpringApplication.run(ReservaHotelesApplication.class, args);
	}

}
