package com.yourdomain.librarysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Main entry point for the Library System Spring Boot application.
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class LibrarySystemApplication {
    public static void main(String[] args) {
        // Ensure MongoDB Java driver uses dnsjava for SRV/TXT lookups in container environments
        // This complements the Dockerfile JVM option and works even if the property is not passed.
        System.setProperty("com.mongodb.dns.resolver", "dnsjava");
        SpringApplication.run(LibrarySystemApplication.class, args);
    }
}
