package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

    HttpRequest msg;

    private String msgExample =
            """
                    GET /index.html HTTP/1.1
                    Host: localhost:8080
                    Connection: keep-alive
                    Accept: */*

                    """;

    @BeforeEach
    void setMsg(){
        msg = new HttpRequest(msgExample);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 method인 GET을 추출한다")
    void getMethodTest() {
        assertThat(msg.getMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 request target인 \"/index.html\"을 추출한다")
    void getRequestTargetTest() {
        assertThat(msg.getRequestTarget()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 Request Line과 header를 추출한다")
    void getHeadersTest() {
        String answer = """
                GET /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Accept: */*""";
        assertThat(msg.getHeaders()).isEqualTo(answer);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 body 부분을 추출한다")
    void getBodyTest() {
        String answer = "";
        assertThat(msg.getBody()).isEqualTo(answer);
    }
}
