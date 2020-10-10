package ru.made.flitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.made.flitter.utils.CollectionTestUtils.assertMapsEqualByKeys;
import static ru.made.flitter.utils.CollectionTestUtils.assertSetEquals;
import static ru.made.flitter.utils.CollectionTestUtils.castToMap;
import static ru.made.flitter.utils.CollectionTestUtils.castToMaps;
import static ru.made.flitter.utils.TestConstants.CONTENT;
import static ru.made.flitter.utils.TestConstants.LOCALHOST;
import static ru.made.flitter.utils.TestConstants.PUBLISHER_NAME;
import static ru.made.flitter.utils.TestConstants.SUBSCRIBER_TOKEN;
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
    public void test_listUsers_empty() {
        var users = listUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void test_addUser_noCheck() {
        addUser("Sasha");
    }

    @Test
    public void test_addUser_singleUser() {
        addUser("Sasha");
        var users = listUsers();
        assertEquals(
                List.of("Sasha"),
                users
        );
    }

    @Test
    public void test_addUser_multipleUsers() {
        var inputUsers = List.of("Sasha", "Nikita");

        for (var user : inputUsers) {
            addUser(user);
        }

        var listedUsers = listUsers();
        assertSetEquals(inputUsers, listedUsers);
    }

    @Test
    public void test_listAllFlits_empty() {
        var flits = listAllFlits();
        assertEquals(0, flits.size());
    }

    @Test
    public void test_addFlit_noCheck() {
        var token = addUser("Sasha");
        var content = "My first flit";

        addFlit(token, content);
    }

    @Test
    public void test_addFlit_singleUser_singleFlit() {
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
    public void test_addFlit_singleUser_multipleFlits() {
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
    public void test_addFlit_multipleUsers_multipleFlits() {
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
    public void test_listFlitByUser_singleUser() {
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
    public void test_listFlitByUser_multipleUsers_multipleFlits() {
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

    @Test
    void test_listSubscribers_empty() {
        var token = addUser("Sasha");
        var subscribers = listSubscribers(token);
        assertTrue(subscribers.isEmpty());
    }

    @Test
    void test_listPublishers_empty() {
        var token = addUser("Sasha");
        var publishers = listPublishers(token);
        assertTrue(publishers.isEmpty());
    }

    @Test
    void test_subscribe_singlePublisher_singleSubscriber() {
        var sashaToken = addUser("Sasha");
        var nikitaToken = addUser("Nikita");

        subscribe(sashaToken, "Nikita");

        assertSetEquals(
                List.of("Nikita"),
                listPublishers(sashaToken)
        );
        assertSetEquals(
                List.of("Sasha"),
                listSubscribers(nikitaToken)
        );

        unsubscribe(sashaToken, "Nikita");

        assertSetEquals(
                Collections.emptyList(),
                listPublishers(sashaToken)
        );
        assertSetEquals(
                Collections.emptyList(),
                listSubscribers(nikitaToken)
        );
    }

    @Test
    void test_subscribe_singlePublisher_multipleSubscribers() {
        var subscriberNames = List.of(
                "SubOne",
                "SubTwo",
                "SubThree"
        );
        var subscriberTokens = subscriberNames
                .stream()
                .map(name -> addUser(name))
                .collect(Collectors.toList());
        var publisherToken = addUser("Pub");

        for (String subscriberToken : subscriberTokens) {
            subscribe(subscriberToken, "Pub");
        }

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    List.of("Pub"),
                    listPublishers(subscriberToken)
            );
        }
        assertSetEquals(
                subscriberNames,
                listSubscribers(publisherToken)
        );

        for (String subscriberToken : subscriberTokens) {
            unsubscribe(subscriberToken, "Pub");
        }

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    listPublishers(subscriberToken)
            );
        }
        assertSetEquals(
                Collections.emptyList(),
                listSubscribers(publisherToken)
        );
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

    private List<String> listSubscribers(String token) {
        var endpoint = LOCALHOST + port + "/subscribers/list/" + token;
        var subscribers = restTemplate.getForObject(endpoint, String[].class);
        return List.of(subscribers);
    }

    private List<String> listPublishers(String token) {
        var endpoint = LOCALHOST + port + "/publishers/list/" + token;
        var subscribers = restTemplate.getForObject(endpoint, String[].class);
        return List.of(subscribers);
    }

    private void subscribe(String subscriberToken, String publisherName) {
        var endpoint = LOCALHOST + port + "/subscribe";
        Map<String, Object> params = Map.of(
                SUBSCRIBER_TOKEN, subscriberToken,
                PUBLISHER_NAME, publisherName
        );
        restTemplate.postForObject(endpoint, params, Object.class);
    }

    private void unsubscribe(String subscriberToken, String publisherName) {
        var endpoint = LOCALHOST + port + "/unsubscribe";
        Map<String, Object> params = Map.of(
                SUBSCRIBER_TOKEN, subscriberToken,
                PUBLISHER_NAME, publisherName
        );
        restTemplate.postForObject(endpoint, params, Object.class);
    }
}
