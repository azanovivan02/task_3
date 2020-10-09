package ru.made.flitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();

        Arrays.stream(beanDefinitionNames)
                .sorted()
                .forEach(System.out::println);
    }

}
