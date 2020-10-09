package ru.made.flitter;

import org.springframework.stereotype.Component;
import ru.made.flitter.dto.Flit;
import ru.made.flitter.dto.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class StateHolder {

    private final ConcurrentMap<String, User> tokenToUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, User> nameToUserMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Flit>> nameToFlitsMap = new ConcurrentHashMap<>();

//    {
//        List<User> sampleUsers = Arrays.asList(
//                new User("Sasha", "1111"),
//                new User("Shamil", "2222")
//        );
//
//        for (User user : sampleUsers) {
//            tokenToUserMap.put(user.getUserToken(), user);
//            nameToUserMap.put(user.getUserName(), user);
//
//            List<Flit> flits = nameToFlitsMap.computeIfAbsent(user.getUserName(), e -> new ArrayList<>());
//
//            for (int i = 0; i < 5; i++) {
//                String flitContent = user.getUserName()+" flit "+i;
//                Flit flit = new Flit(user.getUserName(), flitContent);
//                flits.add(flit);
//            }
//        }
//
//    }

    public ConcurrentMap<String, User> getTokenToUserMap() {
        return tokenToUserMap;
    }

    public ConcurrentMap<String, User> getNameToUserMap() {
        return nameToUserMap;
    }

    public ConcurrentMap<String, List<Flit>> getNameToFlitsMap() {
        return nameToFlitsMap;
    }
}
