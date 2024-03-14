package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestMsgTest {

    HttpRequestMsg msg;

    private String msgExample =
            "GET /index.html HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Accept: */*\n" +
            "\n";

    @BeforeEach
    void setMsg(){
        msg = new HttpRequestMsg(msgExample);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 method인 GET을 추출한다")
    void getMethodTest() {
        assertThat("GET").isEqualTo(msg.getMethod());
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 request target인 \"/index.html\"을 추출한다")
    void getRequestTargetTest() {
        assertThat("/index.html").isEqualTo(msg.getRequestTarget());
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 header를 추출한다")
    void getHeadersTest() {
        String answer = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";
        assertThat(answer).isEqualTo(msg.getHeaders());
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 body 부분을 추출한다")
    void getBodyTest() {
        String answer = "";
        assertThat(answer).isEqualTo(msg.getBody());
    }
}
