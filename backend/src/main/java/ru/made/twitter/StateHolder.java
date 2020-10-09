package ru.made.twitter;

import org.springframework.stereotype.Component;
import ru.made.twitter.dto.Tweet;
import ru.made.twitter.dto.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class StateHolder {

    private final ConcurrentMap<String, User> tokenToUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, User> nameToUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Tweet>> nameToTweetsMap = new ConcurrentHashMap<>();

    {
        List<User> sampleUsers = Arrays.asList(
                new User("Sasha", "1111"),
                new User("Shamil", "2222")
        );

        for (User user : sampleUsers) {
            tokenToUserMap.put(user.getUserToken(), user);
            nameToUserMap.put(user.getUserName(), user);

            List<Tweet> tweets = nameToTweetsMap.computeIfAbsent(user.getUserName(), e -> new ArrayList<>());

            for (int i = 0; i < 5; i++) {
                String tweetContent = user.getUserName()+" tweet "+i;
                Tweet tweet = new Tweet(user.getUserName(), tweetContent);
                tweets.add(tweet);
            }
        }

    }

    public ConcurrentMap<String, User> getTokenToUserMap() {
        return tokenToUserMap;
    }

    public ConcurrentMap<String, User> getNameToUserMap() {
        return nameToUserMap;
    }

    public ConcurrentMap<String, List<Tweet>> getNameToTweetsMap() {
        return nameToTweetsMap;
    }
}
