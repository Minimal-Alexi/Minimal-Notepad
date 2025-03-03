package org.metropolia.minimalnotepad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MinimalnotepadApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        SpringApplication.run(MinimalnotepadApplication.class, args);
    }

}
