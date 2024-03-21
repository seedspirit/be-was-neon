package webserver.session;

import db.SessionDatabase;

public class Session {
    private final String sessionId;
    private final String path;

    public Session() {
        this.sessionId = generateSessionId();
        this.path = "/";
    }

    private String generateSessionId() {
        String sessionId = generateRandomId();
        while (SessionDatabase.isSessionIdAlreadyExists(sessionId)){
            sessionId = generateRandomId();
        }
        return sessionId;
    }

    private String generateRandomId(){
        long randomLong = (long) (Math.random() * 10000000000L) + 1;
        return String.valueOf(randomLong);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return String.format("sid=%s; Path=/", sessionId);
    }
}
