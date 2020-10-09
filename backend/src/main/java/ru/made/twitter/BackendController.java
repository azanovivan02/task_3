package ru.made.twitter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BackendController {

    @GetMapping("/greeting")
    String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println("Invoked greeting: "+name);
        return "Hello "+name;
    }
}
