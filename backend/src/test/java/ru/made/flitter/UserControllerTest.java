package ru.made.flitter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.made.flitter.utils.CollectionTestUtils.assertSetEquals;
import static ru.made.flitter.utils.CollectionTestUtils.castToMap;
import static ru.made.flitter.utils.CollectionTestUtils.castToMaps;
import static ru.made.flitter.utils.CollectionTestUtils.mapOf;
import static ru.made.flitter.utils.TestConstants.CONTENT;
import static ru.made.flitter.utils.TestConstants.LOCALHOST;
import static ru.made.flitter.utils.TestConstants.USER_NAME;
import static ru.made.flitter.utils.TestConstants.USER_TOKEN;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        clear();
    }

    @Test
    public void listUsers_empty() {
        List<String> users = listUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void addUser_noCheck() {
        addUser("Sasha");
    }

    @Test
    public void addUser_singleUser() {
        addUser("Sasha");
        List<String> users = listUsers();
        assertEquals(
                asList("Sasha"),
                users
        );
    }

    @Test
    public void addUser_multipleUsers() {
        List<String> inputUsers = asList("Sasha", "Nikita");

        for (String user : inputUsers) {
            addUser(user);
        }

        List<String> listedUsers = listUsers();
        assertSetEquals(inputUsers, listedUsers);
    }

    @Test
    public void listAllFlits_empty() {
        List<Map<String, Object>> flits = listAllFlits();
        assertEquals(0, flits.size());
    }

    @Test
    public void addFlit_noCheck() {
        String token = addUser("Sasha");
        String content = "My first flit";

        addFlit(token, content);
    }

    @Test
    public void addFlit_singleUser_singleFlit() {
        String token = addUser("Sasha");
        String inputContent = "My first flit";

        addFlit(token, inputContent);
        List<Map<String, Object>> flits = listAllFlits();

        List<Map<String, String>> expectedFlits = asList(
                mapOf(USER_NAME, "Sasha", CONTENT, "My first flit")
        );
        assertSetEquals(expectedFlits, flits);
    }

    @Test
    public void addFlit_singleUser_multipleFlits() {
        String token = addUser("Sasha");
        addFlit(token, "My first flit");
        addFlit(token, "My second flit");

        List<Map<String, Object>> flits = listAllFlits();

        List<Map<String, String>> expectedFlits = asList(
                mapOf(USER_NAME, "Sasha", CONTENT, "My first flit"),
                mapOf(USER_NAME, "Sasha", CONTENT, "My second flit")
        );
        assertSetEquals(expectedFlits, flits);
    }

    @Test
    public void addFlit_multipleUsers_multipleFlits() {
        String tokenOne = addUser("Sasha");
        addFlit(tokenOne, "Sasha's first flit");
        addFlit(tokenOne, "Sasha's second flit");

        String tokenTwo = addUser("Nikita");
        addFlit(tokenTwo, "Nikita's first flit");
        addFlit(tokenTwo, "Nikita's second flit");

        List<Map<String, Object>> flits = listAllFlits();

        List<Map<String, String>> expectedFlits = asList(
                mapOf(USER_NAME, "Sasha", CONTENT, "Sasha's first flit"),
                mapOf(USER_NAME, "Sasha", CONTENT, "Sasha's second flit"),
                mapOf(USER_NAME, "Nikita", CONTENT, "Nikita's first flit"),
                mapOf(USER_NAME, "Nikita", CONTENT, "Nikita's second flit")
        );
        assertSetEquals(expectedFlits, flits);
    }

    @Test
    public void listFlitByUser_singleUser() {
        String token = addUser("Sasha");
        addFlit(token, "My first flit");
        addFlit(token, "My second flit");

        List<Map<String, Object>> flits = listFlitsByUser("Sasha");

        List<Map<String, String>> expectedFlits = asList(
                mapOf(USER_NAME, "Sasha", CONTENT, "My first flit"),
                mapOf(USER_NAME, "Sasha", CONTENT, "My second flit")
        );
        assertSetEquals(expectedFlits, flits);
    }

    @Test
    public void listFlitByUser_multipleUsers_multipleFlits() {
        String tokenOne = addUser("Sasha");
        addFlit(tokenOne, "Sasha's first flit");
        addFlit(tokenOne, "Sasha's second flit");

        String tokenTwo = addUser("Nikita");
        addFlit(tokenTwo, "Nikita's first flit");
        addFlit(tokenTwo, "Nikita's second flit");

        List<Map<String, Object>> flitsBySasha = listFlitsByUser("Sasha");
        List<Map<String, String>> expectedFlitsBySasha = asList(
                mapOf(USER_NAME, "Sasha", CONTENT, "Sasha's first flit"),
                mapOf(USER_NAME, "Sasha", CONTENT, "Sasha's second flit")
        );
        assertSetEquals(expectedFlitsBySasha, flitsBySasha);

        List<Map<String, Object>> flitsByNikita = listFlitsByUser("Nikita");
        List<Map<String, String>> expectedFlitsByNikita = asList(
                mapOf(USER_NAME, "Nikita", CONTENT, "Nikita's first flit"),
                mapOf(USER_NAME, "Nikita", CONTENT, "Nikita's second flit")
        );
        assertSetEquals(expectedFlitsByNikita, flitsByNikita);
    }

    // === Util methods =====================================

    private void clear() {
        String endpoint = LOCALHOST + port + "clear";
        restTemplate.delete(endpoint);
    }

    private List<String> listUsers() {
        String endpoint = LOCALHOST + port + "/user/list";
        String[] users = restTemplate.getForObject(endpoint, String[].class);
        return Arrays.asList(users);
    }

    private String addUser(String name) {
        String endpoint = LOCALHOST + port + "/user/add";
        Map<String, Object> params = mapOf(USER_NAME, name);

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
        String endpoint = LOCALHOST + port + "/flit/add";
        Map<String, Object> params = mapOf(
                USER_TOKEN, token,
                CONTENT, content
        );

        Boolean response = restTemplate.postForObject(endpoint, params, Boolean.class);
        assertTrue(response);
    }

    private List<Map<String, Object>> listAllFlits() {
        String endpoint = LOCALHOST + port + "/flit/list";
        List<Map<String, Object>> flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }

    private List<Map<String, Object>> listFlitsByUser(String name) {
        String endpoint = LOCALHOST + port + "/flit/list/" + name;
        List<Map<String, Object>> flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }
}
