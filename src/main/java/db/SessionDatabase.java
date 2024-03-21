package db;

import model.User;
import util.Pair;
import webserver.session.Session;

import java.util.*;

public class SessionDatabase {
    private static final Map<String, Pair<Session, User>> sessions = new HashMap<>();

    public static void addSession(Session session, User user) {
        Pair<Session, User> sessionPair = Pair.of(session, user);
        sessions.put(session.getSessionId(), sessionPair);
    }

    public static User findUserBySessionId(String sessionId) {
        Pair<Session, User> sessionPair = sessions.get(sessionId);
        return sessionPair.value2();
    }

    public static boolean isSessionIdExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public static void removeRecordOf(String sessionId) {
        sessions.remove(sessionId);
    }
}
