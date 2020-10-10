package ru.made.flitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.made.flitter.dto.SubscribeForm;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

@RestController
public class SubscriptionController {

    @Autowired
    StateHolder holder;

    @PostMapping("/subscribe")
    public ResponseEntity<Boolean> add(@RequestBody SubscribeForm form) {
        var subscriberToken = form.getSubscriberToken();
        if (!holder.getTokenToUserMap().containsKey(subscriberToken)) {
            return ResponseEntity.badRequest().body(false);
        }

        var subscriberName = holder
                .getTokenToUserMap()
                .get(subscriberToken)
                .getUserName();
        var publisherName = form.getPublisherName();

        var subscribers = holder
                .getPublisherNameToSubscribersMap()
                .computeIfAbsent(publisherName, e -> new LinkedHashSet<>());
        subscribers.add(subscriberName);

        var publishers = holder
                .getSubscriberNameToPublishersMap()
                .computeIfAbsent(subscriberName, e -> new LinkedHashSet<>());
        publishers.add(publisherName);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Boolean> delete(@RequestBody SubscribeForm form) {
        var subscriberToken = form.getSubscriberToken();
        if (!holder.getTokenToUserMap().containsKey(subscriberToken)) {
            return ResponseEntity.badRequest().body(false);
        }

        var subscriberName = holder
                .getTokenToUserMap()
                .get(subscriberToken)
                .getUserName();
        var publisherName = form.getPublisherName();

        var subscribers = holder
                .getPublisherNameToSubscribersMap()
                .computeIfAbsent(publisherName, e -> new LinkedHashSet<>());
        subscribers.remove(subscriberName);

        var publishers = holder
                .getSubscriberNameToPublishersMap()
                .computeIfAbsent(subscriberName, e -> new LinkedHashSet<>());
        publishers.remove(publisherName);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/subscribers/list/{userToken}")
    public ResponseEntity<Set<String>> getSubscribers(@PathVariable String userToken) {
        if (!holder.getTokenToUserMap().containsKey(userToken)) {
            return ResponseEntity.badRequest().body(emptySet());
        }

        var userName = holder
                .getTokenToUserMap()
                .get(userToken)
                .getUserName();

        var subscribers = holder
                .getPublisherNameToSubscribersMap()
                .getOrDefault(userName, emptySet());

        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/publishers/list/{userToken}")
    public ResponseEntity<Set<String>> getPublishers(@PathVariable String userToken) {
        if (!holder.getTokenToUserMap().containsKey(userToken)) {
            return ResponseEntity.badRequest().body(emptySet());
        }

        var userName = holder
                .getTokenToUserMap()
                .get(userToken)
                .getUserName();

        var publishers = holder
                .getSubscriberNameToPublishersMap()
                .getOrDefault(userName, emptySet());

        return ResponseEntity.ok(publishers);
    }
}
