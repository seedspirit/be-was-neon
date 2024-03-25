package db;

import model.User;
import util.Pair;
import webserver.session.Cookie;

import java.util.*;

public class SessionDatabase {
    private static final Map<String, Pair<Cookie, User>> sessions = new HashMap<>();

    public static void addRecord(Cookie cookie, User user) {
        Pair<Cookie, User> sessionPair = Pair.of(cookie, user);
        sessions.put(cookie.getSessionId(), sessionPair);
    }

    public static User findUserBySessionId(String sessionId) {
        Pair<Cookie, User> sessionPair = sessions.get(sessionId);
        return sessionPair.value2();
    }

    public static boolean isSessionIdExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public static void removeRecordOf(String sessionId) {
        sessions.remove(sessionId);
    }
}
