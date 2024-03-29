package webserver.httpMessage.httpRequest;

import db.SessionDatabase;
import db.UserDatabase;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.session.Cookie;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestHeadersTest {
    private RequestHeaders requestHeaders;
    private RequestHeaders falseRequestHeaders;
    private RequestHeaders falseButWithSidHeaders;

    @BeforeEach
    void makeMockHeaders(){
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", List.of("application/x-www-form-urlencoded"));
        headers.put("Content-Length", List.of("99"));
        headers.put("Cookie", List.of("sid=1234567", "chocoCookie=987654"));
        this.requestHeaders = new RequestHeaders(headers);

        Map<String, List<String>> falseHeaders = new HashMap<>();
        falseHeaders.put("Content-Type", List.of("multipart/form-data"));
        falseHeaders.put("Content-Length", List.of("99"));
        falseHeaders.put("Cookie", List.of("chocoCookie=987654"));
        this.falseRequestHeaders = new RequestHeaders(falseHeaders);

        Map<String, List<String>> falseWithSidHeaders = new HashMap<>();
        falseWithSidHeaders.put("Content-Type", List.of("multipart/form-data"));
        falseWithSidHeaders.put("Content-Length", List.of("99"));
        falseWithSidHeaders.put("Cookie", List.of("sid=1234", "chocoCookie=987654"));
        this.falseButWithSidHeaders = new RequestHeaders(falseWithSidHeaders);

        SessionDatabase.clearDatabase();
        UserDatabase.clearDatabase();
    }

    @DisplayName("header의 content-length인 99를 정수 타입으로 변환하여 반환한다")
    @Test
    void getContentLengthTest() {
        assertThat(requestHeaders.getContentLength()).isEqualTo(99);
    }

    @DisplayName("header에서 Content-Type이 x-www-form-urlencoded 형태인지 여부에 따라 true/false를 각각 반환한다")
    @Test
     void contentTypeEqualsFormURLEncodedTest() {
        assertThat(requestHeaders.contentTypeEqualsFormURLEncoded()).isTrue();
        assertThat(falseRequestHeaders.contentTypeEqualsFormURLEncoded()).isFalse();
    }

    @DisplayName("sid=으로 시작하는 세션ID가 있는 경우 true를, 없을 경우 false를 반환한다")
    @Test
    void hasLoginCookieTest() {
        assertThat(requestHeaders.hasLoginCookie()).isTrue();
        assertThat(falseRequestHeaders.hasLoginCookie()).isFalse();
    }

    @DisplayName("sid=으로 시작하는 세션ID가 있는 경우 그 값을, 없을 경우 NoSuchElementException을 반환한다")
    @Test
    void getLoginCookieSessionIdTest() {
        assertThat(requestHeaders.getLoginCookieSessionId()).isEqualTo("1234567");
        assertThatThrownBy(() -> falseRequestHeaders.getLoginCookieSessionId())
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("sid=로 시작하는 세션ID가 없거나, 있음에도 DB에 저장되어 있지 않은 경우 false를, 두 조건을 모두 만족하면 true를 반환한다")
    @Test
    void sessionIdExistsInSessionDB() throws NoSuchFieldException, IllegalAccessException {
        setupSessionDB();
        assertThat(falseRequestHeaders.sessionIdExistsInSessionDB()).isFalse();
        assertThat(falseButWithSidHeaders.sessionIdExistsInSessionDB()).isFalse();
        assertThat(requestHeaders.sessionIdExistsInSessionDB()).isTrue();
    }

    private void setupSessionDB() throws NoSuchFieldException, IllegalAccessException {
        User user = new User("javajigi", "password", "박재성", "e@e.com");
        UserDatabase.addUser(user);
        Class<Cookie> clazz = Cookie.class;
        Field sessionId = clazz.getDeclaredField("sessionId");
        sessionId.setAccessible(true);
        Cookie cookie = new Cookie();
        sessionId.set(cookie, "1234567");
        SessionDatabase.addRecord(cookie, user);
    }
}