package ru.made.flitter;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.made.flitter.utils.FlitterRestWrapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    private FlitterRestWrapper rest;

    public RestControllerTest(
            @Autowired TestRestTemplate restTemplate,
            @LocalServerPort int port
    ) {
        this.rest = new FlitterRestWrapper(restTemplate, port);
    }

    @BeforeEach
    void setUp() {
        rest.clear();
    }
}
