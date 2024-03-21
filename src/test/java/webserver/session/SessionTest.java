package webserver.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Session;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionTest {

    @DisplayName("세션id가 100인 Session의 toString을 호출하면 sid=100; Path=/을 반환한다")
    @Test
    void sessionToStringTest() throws NoSuchFieldException, IllegalAccessException {
        Class<Session> clazz = Session.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Session session = new Session();
        sessionId.set(session, "100");
        String expected = "sid=100; Path=/";
        assertThat(session.toString()).isEqualTo(expected);
    }
}
