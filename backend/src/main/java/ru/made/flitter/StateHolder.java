package ru.made.flitter;

import org.springframework.stereotype.Component;
import ru.made.flitter.dto.Flit;
import ru.made.flitter.dto.User;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class StateHolder {

    private final ConcurrentMap<String, User> tokenToUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, User> nameToUserMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, List<Flit>> nameToFlitsMap = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Set<String>> subscriberNameToPublishersMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Set<String>> publisherNameToSubscribersMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, User> getTokenToUserMap() {
        return tokenToUserMap;
    }

    public ConcurrentMap<String, User> getNameToUserMap() {
        return nameToUserMap;
    }

    public ConcurrentMap<String, List<Flit>> getNameToFlitsMap() {
        return nameToFlitsMap;
    }

    public ConcurrentMap<String, Set<String>> getSubscriberNameToPublishersMap() {
        return subscriberNameToPublishersMap;
    }

    public ConcurrentMap<String, Set<String>> getPublisherNameToSubscribersMap() {
        return publisherNameToSubscribersMap;
    }
}
