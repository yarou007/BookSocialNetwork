package com.yarou.book;

import com.yarou.book.role.Role;
import com.yarou.book.role.roleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner (roleRepository RoleRepository){
		return args -> {
			if (RoleRepository.findByName("USER").isEmpty()){
				RoleRepository.save(
						Role.builder().name("USER").build()
				);
			}
		};
	}
}
