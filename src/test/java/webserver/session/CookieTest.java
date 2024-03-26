package webserver.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieTest {

    @DisplayName("세션id가 100인 Session의 toString을 호출하면 sid=100; Path=/; Max-Age=3600을 반환한다")
    @Test
    void sessionToStringTest() throws NoSuchFieldException, IllegalAccessException {
        Class<Cookie> clazz = Cookie.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Cookie cookie = new Cookie();
        sessionId.set(cookie, "100");
        String expected = "sid=100; Path=/; Max-Age=3600";
        assertThat(cookie.toString()).isEqualTo(expected);
    }
}
