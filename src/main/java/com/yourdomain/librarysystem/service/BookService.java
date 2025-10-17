package com.yourdomain.librarysystem.service;

import com.yourdomain.librarysystem.model.Book;
import com.yourdomain.librarysystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        bookRepository.findAll().forEach(list::add);
        return list;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public java.util.Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
