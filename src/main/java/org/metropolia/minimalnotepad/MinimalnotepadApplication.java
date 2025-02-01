package org.metropolia.minimalnotepad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MinimalnotepadApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Loads .env file
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD")); // Sets DB_PASSWORD environment variable

        SpringApplication.run(MinimalnotepadApplication.class, args);
    }

}
