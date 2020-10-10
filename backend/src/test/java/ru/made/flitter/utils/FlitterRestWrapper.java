package ru.made.flitter.utils;

import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.made.flitter.utils.CollectionTestUtils.castToMap;
import static ru.made.flitter.utils.CollectionTestUtils.castToMaps;
import static ru.made.flitter.utils.TestConstants.CONTENT;
import static ru.made.flitter.utils.TestConstants.LOCALHOST;
import static ru.made.flitter.utils.TestConstants.PUBLISHER_NAME;
import static ru.made.flitter.utils.TestConstants.SUBSCRIBER_TOKEN;
import static ru.made.flitter.utils.TestConstants.USER_NAME;
import static ru.made.flitter.utils.TestConstants.USER_TOKEN;

public class FlitterRestWrapper {

    private final TestRestTemplate restTemplate;
    private final int port;

    public FlitterRestWrapper(TestRestTemplate restTemplate, int port) {
        this.restTemplate = restTemplate;
        this.port = port;
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

    public int getPort() {
        return port;
    }

    public void clear() {
        var endpoint = LOCALHOST + port + "clear";
        restTemplate.delete(endpoint);
    }

    public List<String> listUsers() {
        var endpoint = LOCALHOST + port + "/user/list";
        var users = restTemplate.getForObject(endpoint, String[].class);
        return List.of(users);
    }

    public String addUser(String name) {
        var endpoint = LOCALHOST + port + "/user/register";
        var params = Map.of(USER_NAME, name);

        var response = castToMap(
                restTemplate.postForObject(endpoint, params, Object.class)
        );

        var outputName = response.get(USER_NAME);
        var outputToken = response.get(USER_TOKEN);
        assertEquals(name, outputName);
        assertNotNull(outputToken);

        return outputToken.toString();
    }

    public List<String> addAllUsers(Collection<String> userNames) {
        var userTokens = userNames
                .stream()
                .map(name -> addUser(name))
                .collect(Collectors.toList());
        return userTokens;
    }

    public void addFlit(String token, String content) {
        var endpoint = LOCALHOST + port + "/flit/add";
        var params = Map.of(
                USER_TOKEN, token,
                CONTENT, content
        );

        Boolean response = restTemplate.postForObject(endpoint, params, Boolean.class);
        assertTrue(response);
    }

    public List<Map<String, Object>> listAllFlits() {
        var endpoint = LOCALHOST + port + "/flit/list";
        var flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }

    public List<Map<String, Object>> listFlitsByUser(String name) {
        var endpoint = LOCALHOST + port + "/flit/list/" + name;
        var flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }

    public List<String> listSubscribers(String token) {
        var endpoint = LOCALHOST + port + "/subscribers/list/" + token;
        var subscribers = restTemplate.getForObject(endpoint, String[].class);
        return List.of(subscribers);
    }

    public List<String> listPublishers(String token) {
        var endpoint = LOCALHOST + port + "/publishers/list/" + token;
        var subscribers = restTemplate.getForObject(endpoint, String[].class);
        return List.of(subscribers);
    }

    public void subscribe(String subscriberToken, String publisherName) {
        var endpoint = LOCALHOST + port + "/subscribe";
        var params = Map.of(
                SUBSCRIBER_TOKEN, subscriberToken,
                PUBLISHER_NAME, publisherName
        );
        restTemplate.postForObject(endpoint, params, Object.class);
    }

    public void subscribeAll(Collection<String> subscriberTokens, List<String> publisherNames) {
        for (String publisherName : publisherNames) {
            for (String subscriberToken : subscriberTokens) {
                subscribe(subscriberToken, publisherName);
            }
        }
    }

    public void unsubscribe(String subscriberToken, String publisherName) {
        var endpoint = LOCALHOST + port + "/unsubscribe";
        var params = Map.of(
                SUBSCRIBER_TOKEN, subscriberToken,
                PUBLISHER_NAME, publisherName
        );
        restTemplate.postForObject(endpoint, params, Object.class);
    }

    public void unsubscribeAll(Collection<String> subscriberTokens, List<String> publisherNames) {
        for (String publisherName : publisherNames) {
            for (String subscriberToken : subscriberTokens) {
                unsubscribe(subscriberToken, publisherName);
            }
        }
    }

    public List<Map<String, Object>> listFlitsConsumedByUser(String token) {
        var endpoint = LOCALHOST + port + "/flit/list/feed/" + token;
        var flits = castToMaps(
                restTemplate.getForObject(endpoint, Object[].class)
        );
        return flits;
    }
}
