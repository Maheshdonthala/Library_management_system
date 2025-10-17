package com.yourdomain.librarysystem.controller;

import com.yourdomain.librarysystem.model.Book;
import com.yourdomain.librarysystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String getBooks(Model model) {
        try {
            model.addAttribute("books", bookService.getAllBooks());
        } catch (Exception ex) {
            // If the DB is unreachable, show an empty list and an error message instead of failing with 500
            model.addAttribute("books", Collections.emptyList());
            model.addAttribute("errorMessage", "Unable to retrieve books: " + ex.getMessage());
        }
        return "books";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/books";
    }

    @GetMapping("/add-book")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add-book")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("successMessage", "Book saved successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save book: " + ex.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete book: " + ex.getMessage());
        }
        return "redirect:/books";
    }
}
