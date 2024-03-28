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

    public static Pair<Cookie, User> findCookieUserPairBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public static User findUserBySessionId(String sessionId) {
        Pair<Cookie, User> sessionPair = sessions.get(sessionId);
        return sessionPair.getUser();
    }

    public static boolean isSessionIdExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public static void removeRecordOf(String sessionId) {
        sessions.remove(sessionId);
    }

    public static void clearDatabase(){
        sessions.clear();
    }
}
