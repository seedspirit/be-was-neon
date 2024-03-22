package db;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Cookie;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDatabaseTest {

    @DisplayName("세션 ID로 데이터베이스에서 특정 유저 객체를 찾을 수 있다")
    @Test
    void findUserBySessionIdTest() throws NoSuchFieldException, IllegalAccessException {
        Cookie cookie = getMockSession();
        User user = new User("ex", "ex", "ex", "ex@email.com");
        SessionDatabase.addCookie(cookie, user);
        String sessionId = "100";
        assertThat(SessionDatabase.findUserBySessionId(sessionId)).isEqualTo(user);
    }

    private Cookie getMockSession() throws NoSuchFieldException, IllegalAccessException {
        Class<Cookie> clazz = Cookie.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Cookie cookie = new Cookie();
        sessionId.set(cookie, "100");
        return cookie;
    }
}