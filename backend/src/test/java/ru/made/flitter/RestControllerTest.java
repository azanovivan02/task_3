package ru.made.flitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.made.flitter.utils.CollectionTestUtils.assertMapsEqualByKeys;
import static ru.made.flitter.utils.CollectionTestUtils.assertSetEquals;
import static ru.made.flitter.utils.CollectionTestUtils.castToMap;
import static ru.made.flitter.utils.CollectionTestUtils.castToMaps;
import static ru.made.flitter.utils.TestConstants.CONTENT;
import static ru.made.flitter.utils.TestConstants.LOCALHOST;
import static ru.made.flitter.utils.TestConstants.USER_NAME;
import static ru.made.flitter.utils.TestConstants.USER_TOKEN;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        clear();
    }

    @Test
    public void listUsers_empty() {
        var users = listUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void addUser_noCheck() {
        addUser("Sasha");
    }

    @Test
    public void addUser_singleUser() {
        addUser("Sasha");
        var users = listUsers();
        assertEquals(
                List.of("Sasha"),
                users
        );
    }

    @Test
    public void addUser_multipleUsers() {
        var inputUsers = List.of("Sasha", "Nikita");

        for (var user : inputUsers) {
            addUser(user);
        }

        var listedUsers = listUsers();
        assertSetEquals(inputUsers, listedUsers);
    }

    @Test
    public void listAllFlits_empty() {
        var flits = listAllFlits();
        assertEquals(0, flits.size());
    }

    @Test
    public void addFlit_noCheck() {
        var token = addUser("Sasha");
        var content = "My first flit";

        addFlit(token, content);
    }

    @Test
    public void addFlit_singleUser_singleFlit() {
        var token = addUser("Sasha");
        var inputContent = "My first flit";

        addFlit(token, inputContent);
        var flits = listAllFlits();

        var expectedFlits = List.of(
                Map.of(USER_NAME, "Sasha", CONTENT, "My first flit")
        );
        assertMapsEqualByKeys(expectedFlits, flits, USER_NAME, CONTENT);
    }

    @Test
    public void addFlit_singleUser_multipleFlits() {
        var token = addUser("Sasha");
        addFlit(token, "My first flit");
        addFlit(token, "My second flit");

        var flits = listAllFlits();

        var expectedFlits = List.of(
                Map.of(USER_NAME, "Sasha", CONTENT, "My first flit"),
                Map.of(USER_NAME, "Sasha", CONTENT, "My second flit")
        );
        assertMapsEqualByKeys(expectedFlits, flits, USER_NAME, CONTENT);
    }

    @Test
    public void addFlit_multipleUsers_multipleFlits() {
        var tokenOne = addUser("Sasha");
        addFlit(tokenOne, "Sasha's first flit");
        addFlit(tokenOne, "Sasha's second flit");

        var tokenTwo = addUser("Nikita");
        addFlit(tokenTwo, "Nikita's first flit");
        addFlit(tokenTwo, "Nikita's second flit");

        var flits = listAllFlits();

        var expectedFlits = List.of(
                Map.of(USER_NAME, "Sasha", CONTENT, "Sasha's first flit"),
                Map.of(USER_NAME, "Sasha", CONTENT, "Sasha's second flit"),
                Map.of(USER_NAME, "Nikita", CONTENT, "Nikita's first flit"),
                Map.of(USER_NAME, "Nikita", CONTENT, "Nikita's second flit")
        );
        assertMapsEqualByKeys(expectedFlits, flits, USER_NAME, CONTENT);
    }

    @Test
    public void listFlitByUser_singleUser() {
        var token = addUser("Sasha");
        addFlit(token, "My first flit");
        addFlit(token, "My second flit");

        var flits = listFlitsByUser("Sasha");

        var expectedFlits = List.of(
                Map.of(USER_NAME, "Sasha", CONTENT, "My first flit"),
                Map.of(USER_NAME, "Sasha", CONTENT, "My second flit")
        );
        assertMapsEqualByKeys(expectedFlits, flits, USER_NAME, CONTENT);
    }

    @Test
    public void listFlitByUser_multipleUsers_multipleFlits() {
        var tokenOne = addUser("Sasha");
        addFlit(tokenOne, "Sasha's first flit");
        addFlit(tokenOne, "Sasha's second flit");

        var tokenTwo = addUser("Nikita");
        addFlit(tokenTwo, "Nikita's first flit");
        addFlit(tokenTwo, "Nikita's second flit");

        var flitsBySasha = listFlitsByUser("Sasha");
        List<Map<String, String>> expectedFlitsBySasha = List.of(
                Map.of(USER_NAME, "Sasha", CONTENT, "Sasha's first flit"),
                Map.of(USER_NAME, "Sasha", CONTENT, "Sasha's second flit")
        );
        assertMapsEqualByKeys(expectedFlitsBySasha, flitsBySasha, USER_NAME, CONTENT);

        var flitsByNikita = listFlitsByUser("Nikita");
        var expectedFlitsByNikita = List.of(
                Map.of(USER_NAME, "Nikita", CONTENT, "Nikita's first flit"),
                Map.of(USER_NAME, "Nikita", CONTENT, "Nikita's second flit")
        );
        assertMapsEqualByKeys(expectedFlitsByNikita, flitsByNikita, USER_NAME, CONTENT);
    }

    // === Util methods ====================================================================

    private void clear() {
        var endpoint = LOCALHOST + port + "clear";
        restTemplate.delete(endpoint);
    }

    private List<String> listUsers() {
        var endpoint = LOCALHOST + port + "/user/list";
        String[] users = restTemplate.getForObject(endpoint, String[].class);
        return List.of(users);
    }

    private String addUser(String name) {
        var endpoint = LOCALHOST + port + "/user/add";
        Map<String, Object> params = Map.of(USER_NAME, name);

        Map<String, Object> response = castToMap(
                restTemplate.postForObject(endpoint, params, Object.class)
        );

        Object outputName = response.get(USER_NAME);
        Object outputToken = response.get(USER_TOKEN);
        assertEquals(name, outputName);
        assertNotNull(outputToken);

        return outputToken.toString();
    }

    private void addFlit(String token, String content) {
        var endpoint = LOCALHOST + port + "/flit/add";
        Map<String, Object> params = Map.of(
                USER_TOKEN, token,
                CONTENT, content
        );

        Boolean response = restTemplate.postForObject(endpoint, params, Boolean.class);
        assertTrue(response);
    }

    private List<Map<String, Object>> listAllFlits() {
        var endpoint = LOCALHOST + port + "/flit/list";
        List<Map<String, Object>> flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }

    private List<Map<String, Object>> listFlitsByUser(String name) {
        var endpoint = LOCALHOST + port + "/flit/list/" + name;
        List<Map<String, Object>> flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }
}
