package db;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Cookie;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDatabaseTest {

    private Cookie cookie;
    private User user;

    @BeforeEach
    void setCookie() throws NoSuchFieldException, IllegalAccessException {
        SessionDatabase.clearDatabase();
        this.cookie = getMockCookie();
        this.user = new User("ex", "ex", "ex", "ex@email.com");
        SessionDatabase.addRecord(cookie, user);
    }

    @DisplayName("세션 ID로 데이터베이스에서 특정 유저 객체를 찾을 수 있다")
    @Test
    void findUserBySessionIdTest() {
        String sessionId = "100";
        assertThat(SessionDatabase.findUserBySessionId(sessionId)).isEqualTo(user);
    }

    @DisplayName("세션 ID로 데이터베이스에서 세션 정보를 삭제할 수 있다")
    @Test
    void recordRemovalTest() {
        String sessionId = "100";
        SessionDatabase.removeRecordOf(sessionId);
        assertThat(SessionDatabase.isSessionIdExists(sessionId)).isFalse();
    }

    private Cookie getMockCookie() throws NoSuchFieldException, IllegalAccessException {
        Class<Cookie> clazz = Cookie.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Cookie cookie = new Cookie();
        sessionId.set(cookie, "100");
        return cookie;
    }
}