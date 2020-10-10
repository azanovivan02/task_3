package ru.made.flitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.made.flitter.utils.FlitterRestWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.made.flitter.utils.CollectionTestUtils.assertMapsEqualByKeys;
import static ru.made.flitter.utils.CollectionTestUtils.assertSetEquals;
import static ru.made.flitter.utils.TestConstants.CONTENT;
import static ru.made.flitter.utils.TestConstants.USER_NAME;
import static ru.made.flitter.utils.TestConstants.USER_TOKEN;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriptionRestTest {

    private FlitterRestWrapper rest;

    public SubscriptionRestTest(
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
    void test_listSubscribers_empty() {
        var token = rest.addUser("Sasha");
        var subscribers = rest.listSubscribers(token);
        assertTrue(subscribers.isEmpty());
    }

    @Test
    void test_listPublishers_empty() {
        var token = rest.addUser("Sasha");
        var publishers = rest.listPublishers(token);
        assertTrue(publishers.isEmpty());
    }

    @Test
    void test_subscribe_singlePublisher_singleSubscriber() {
        var sashaToken = rest.addUser("Sasha");
        var nikitaToken = rest.addUser("Nikita");

        rest.subscribe(sashaToken, "Nikita");

        assertSetEquals(
                List.of("Nikita"),
                rest.listPublishers(sashaToken)
        );
        assertSetEquals(
                List.of("Sasha"),
                rest.listSubscribers(nikitaToken)
        );

        rest.unsubscribe(sashaToken, "Nikita");

        assertSetEquals(
                Collections.emptyList(),
                rest.listPublishers(sashaToken)
        );
        assertSetEquals(
                Collections.emptyList(),
                rest.listSubscribers(nikitaToken)
        );
    }

    @Test
    void test_subscribe_singlePublisher_multipleSubscribers() {
        var subscriberNames = List.of(
                "SubOne",
                "SubTwo",
                "SubThree"
        );
        var subscriberTokens = rest.addAllUsers(subscriberNames);
        var publisherToken = rest.addUser("Pub");
        rest.subscribeAll(subscriberTokens, List.of("Pub"));

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    List.of("Pub"),
                    rest.listPublishers(subscriberToken)
            );
        }
        assertSetEquals(
                subscriberNames,
                rest.listSubscribers(publisherToken)
        );

        rest.unsubscribeAll(subscriberTokens, List.of("Pub"));

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listPublishers(subscriberToken)
            );
        }
        assertSetEquals(
                Collections.emptyList(),
                rest.listSubscribers(publisherToken)
        );
    }

    @Test
    void test_subscribe_multiplePublishers_multipleSubscribers() {
        var subscriberNames = List.of(
                "SubOne",
                "SubTwo",
                "SubThree"
        );
        var publisherNames = List.of(
                "PubOne",
                "PubTwo",
                "PubThree"
        );
        var subscriberTokens = rest.addAllUsers(subscriberNames);
        var publisherTokens = rest.addAllUsers(publisherNames);
        rest.subscribeAll(subscriberTokens, publisherNames);

        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    publisherNames,
                    rest.listPublishers(subscriberToken)
            );
        }
        for (String publisherToken : publisherTokens) {
            assertSetEquals(
                    subscriberNames,
                    rest.listSubscribers(publisherToken)
            );
        }

        rest.unsubscribeAll(subscriberTokens, publisherNames);

        for (String publisherToken : publisherTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listSubscribers(publisherToken)
            );
        }
        for (String subscriberToken : subscriberTokens) {
            assertSetEquals(
                    Collections.emptyList(),
                    rest.listPublishers(subscriberToken)
            );
        }
    }

    @Test
    void test_consumeFlits() {
        var publisherNames = List.of(
                "PubOne",
                "PubTwo",
                "PubThree"
        );

        var subscriberToken = rest.addUser("SubOne");
        var neutralToken = rest.addUser("NeutralOne");
        var publisherTokens = rest.addAllUsers(publisherNames);

        rest.subscribeAll(
                List.of(subscriberToken),
                publisherNames
        );

        var expectedFlits = new ArrayList<Map<String, Object>>();
        for (int publisherIndex = 0; publisherIndex < publisherTokens.size(); publisherIndex++) {
            var publisherName = publisherNames.get(publisherIndex);
            var publisherToken = publisherTokens.get(publisherIndex);

            for (int flitIndex = 0; flitIndex < 3; flitIndex++) {
                var content = String.format("Flit number %s by %s", flitIndex, publisherName);
                rest.addFlit(publisherToken, content);
                expectedFlits.add(Map.of(
                        USER_NAME, publisherName,
                        CONTENT, content
                ));
            }
        }

        // Also adding flits from user "NeutralOne", who is not read by anyone actually
        for (int flitIndex = 0; flitIndex < 3; flitIndex++) {
            var content = String.format("Flit number %s by %s", flitIndex, neutralToken);
            rest.addFlit(neutralToken, content);
        }

        var flitsFromFeed = rest.listFlitsConsumedByUser(subscriberToken);
        assertMapsEqualByKeys(expectedFlits, flitsFromFeed, USER_NAME, USER_TOKEN);
    }
}
