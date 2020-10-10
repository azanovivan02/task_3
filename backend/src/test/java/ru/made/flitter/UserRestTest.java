package ru.made.flitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.made.flitter.utils.FlitterRestWrapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.made.flitter.utils.CollectionTestUtils.assertSetEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTest {

    private FlitterRestWrapper rest;

    public UserRestTest(
            @Autowired TestRestTemplate restTemplate,
            @LocalServerPort int port
    ) {
        this.rest = new FlitterRestWrapper(restTemplate, port);
    }

    @BeforeEach
    void setUp() {
        rest.clear();
    }

    @Test
    public void test_listUsers_empty() {
        var users = rest.listUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void test_addUser_noCheck() {
        rest.addUser("Sasha");
    }

    @Test
    public void test_addUser_singleUser() {
        rest.addUser("Sasha");
        var users = rest.listUsers();
        assertEquals(
                List.of("Sasha"),
                users
        );
    }

    @Test
    public void test_addUser_multipleUsers() {
        var inputUsers = List.of("Sasha", "Nikita");

        for (var user : inputUsers) {
            rest.addUser(user);
        }

        var listedUsers = rest.listUsers();
        assertSetEquals(inputUsers, listedUsers);
    }
}
