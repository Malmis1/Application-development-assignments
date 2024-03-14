package no.ntnu.monolith;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    /**
     * Gets the home page.
     * 
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    /**
     * Gets the books page.
     * 
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/books")
    public String getBooks() {
        return "books";
    }

    /**
     * Gets the about page.
     * 
     * @return name of the ThymeLeaf to render.
     */
    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}
