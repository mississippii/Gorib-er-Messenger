package veer.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebClientApp  {

    public static void main(String[] args) {
        SpringApplication.run(WebClientApp.class, args);
    }
}