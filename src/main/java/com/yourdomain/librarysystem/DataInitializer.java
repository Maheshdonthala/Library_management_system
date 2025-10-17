package com.yourdomain.librarysystem;

import com.yourdomain.librarysystem.model.Book;
import com.yourdomain.librarysystem.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting Library System - DataInitializer");
        try {
            if (bookRepository.count() == 0) {
                bookRepository.save(new Book(null, "The Hobbit", "J.R.R. Tolkien"));
                bookRepository.save(new Book(null, "1984", "George Orwell"));
                bookRepository.save(new Book(null, "Clean Code", "Robert C. Martin"));
                logger.info("Inserted sample books into the repository");
            } else {
                logger.info("Repository already contains {} books", bookRepository.count());
            }
        } catch (Exception ex) {
            // Log and continue startup; database may be unavailable (e.g., local Mongo not running)
            logger.warn("Could not initialize sample data - continuing startup without DB initialization: {}", ex.getMessage());
        }
    }
}
