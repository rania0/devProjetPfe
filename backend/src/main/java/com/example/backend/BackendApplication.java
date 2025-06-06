package com.example.backend;


import com.example.backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

	@SpringBootApplication
	public class BackendApplication implements CommandLineRunner {

		@Autowired
		private UtilisateurService utilisateurService;

		public static void main(String[] args) {
			SpringApplication.run(BackendApplication.class, args);
		}

		@Override
		public void run(String... args) {
			utilisateurService.ajouterAdminParDefaut();
		}
	}
