package no.ntnu.controllers;

import no.ntnu.service.AuthorService;
import no.ntnu.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Autowired
    BookService bookService;
    @Autowired
    AuthorService authorService;

    /**
     * Gets the home page.
     * 
     * @param model the model where the data will be stored.
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("books", bookService.getFirst(2));
        return "index";
    }

    /**
     * Gets the books page.
     * 
     * @param model the model where the data will be stored.
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books";
    }

    /**
     * Gets the about page.
     * 
     * @param model the model where the data will be stored.
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/about")
    public String getAbout(Model model) {
        model.addAttribute("bookCount", bookService.getCount());
        model.addAttribute("authorCount", authorService.getCount());
        return "about";
    }
}
