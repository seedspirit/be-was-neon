package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SessionDataBase {
    private static Map<String, User> sessions = new HashMap<>();

    public static void addSession(String session, User user) {
        sessions.put(session, user);
    }

    public static User findUserBySession(String session) {
        return sessions.get(session);
    }

    public static Collection<User> findAll() {
        return sessions.values();
    }
}
