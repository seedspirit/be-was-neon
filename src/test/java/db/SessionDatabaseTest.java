package db;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Session;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDatabaseTest {

    @DisplayName("세션 ID로 데이터베이스에서 특정 유저 객체를 찾을 수 있다")
    @Test
    void findUserBySessionIdTest() throws NoSuchFieldException, IllegalAccessException {
        Session session = getMockSession();
        User user = new User("ex", "ex", "ex", "ex@email.com");
        SessionDatabase.addSession(session, user);
        assertThat(SessionDatabase.findUserBySessionId("100")).isEqualTo(user);
    }

    private Session getMockSession() throws NoSuchFieldException, IllegalAccessException {
        Class<Session> clazz = Session.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Session session = new Session();
        sessionId.set(session, "100");
        return session;
    }
}