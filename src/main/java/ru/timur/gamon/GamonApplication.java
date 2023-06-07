package ru.timur.gamon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.timur.gamon.services.ComputerService;

@SpringBootApplication
public class GamonApplication {
	public static void main(String[] args) {


		SpringApplication.run(GamonApplication.class, args);
	}

}
