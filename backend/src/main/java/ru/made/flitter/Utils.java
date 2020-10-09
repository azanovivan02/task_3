package ru.made.flitter;

import java.util.UUID;

public class Utils {

    public static String generateUserToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
