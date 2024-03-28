package util;

public class Pair<Cookie, User> {
    private final Cookie cookie;
    private final User user;

    public Pair(Cookie cookie, User user) {
        this.cookie = cookie;
        this.user = user;
    }

    public static <Cookie, User> Pair<Cookie, User> of(Cookie cookie, User user) {
        return new Pair<>(cookie, user);
    }

    public Cookie getCookie() {
        return cookie;
    }

    public User getUser() {
        return user;
    }
}