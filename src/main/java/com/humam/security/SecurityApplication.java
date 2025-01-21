package com.humam.security;

import com.humam.security.auth.AuthenticationService;
import com.humam.security.auth.RegisterRequest;
import com.humam.security.user.Role;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

import static com.humam.security.user.Role.ADMIN;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableAspectJAutoProxy()
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			UserRepository userRepository
	)
	{
		Optional<User> exists = userRepository.findAdmin(ADMIN);
		if (exists.isPresent()) {
			return args -> {

			};
		}
		return args -> {
			var registerRequest = RegisterRequest.builder()
					.first_name("admin")
					.last_name("admin")
					.email("admin@admin.com")
					.password("password")
					.build();

			var auth = service.register(registerRequest);

			var adminUser = userRepository.findById(1).orElse(new User());

			adminUser.setRole(ADMIN);
			adminUser.setEnabled(true);
			userRepository.save(adminUser);

			System.out.println("Admin token is: " + auth.getToken());
		};
	}
}
