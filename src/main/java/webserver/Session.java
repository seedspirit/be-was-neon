package webserver;

public class Session {
    private final String sessionId;
    private final String path;

    public Session() {
        this.sessionId = generateSessionId();
        this.path = "/";
    }

    private String generateSessionId() {
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
