package com.example.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class BookController {

    Logger log = LoggerFactory.getLogger(BookController.class);

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/books")
    public Book save(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping("/books")
    public List<Book> findAll(@RequestHeader(value="X-Appengine-Inbound-Appid") String gcpHeader) {
        log.warn("Here's the header: " + gcpHeader);
        return bookRepository.findAll();
    }

    @PostMapping("/books/random")
    public List<Book> random() {
        Book entity = new Book();
        entity.setAuthor("Stephen");
        entity.setTitle("" + new Random().nextInt(100));
        Book save = bookRepository.save(entity);
        return bookRepository.findAll();
    }
}
