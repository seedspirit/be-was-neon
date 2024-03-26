package webserver.session;

import db.SessionDatabase;

public class Cookie {
    private final String sessionId;
    private final String path;
    private int maxAge;

    public Cookie() {
        this.sessionId = generateSessionId();
        this.path = "/";
        this.maxAge = 3600;
    }

    private String generateSessionId() {
        String sessionId = generateRandomId();
        while (SessionDatabase.isSessionIdExists(sessionId)){
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

    public void invalidateCookie(){
        this.maxAge = -1;
    }

    @Override
    public String toString() {
        return String.format("sid=%s; Path=/; Max-Age=%d", sessionId, maxAge);
    }
}
