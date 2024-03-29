package webserver.httpMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.htttpRequest.body.EmptyBody;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.htttpRequest.factory.RequestFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpConstants.METHOD;

class HttpRequestTest {

    HttpRequest httpRequest;

    private String requestExample =
            """
                    GET /index.html HTTP/1.1
                    Host: localhost:8080
                    Connection: keep-alive
                    Accept: */*

                    """;

    @BeforeEach
    void setMsg(){
        InputStream in = new ByteArrayInputStream(requestExample.getBytes());
        RequestFactory requestFactory = new RequestFactory();
        httpRequest = requestFactory.createHttpRequestFrom(in);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 method인 GET을 추출한다")
    void getMethodTest() {
        assertThat(httpRequest.getRequestLine().getValueOf(METHOD)).isEqualTo("GET");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 request target인 \"/index.html\"을 추출한다")
    void getRequestTargetTest() {
        assertThat(httpRequest.getRequestLine().getRequestTarget()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 header를 key-value 형태로 추출한다")
    void getHeadersTest() {
        Map<String, List<String>> answer = new HashMap<>();
        answer.put("Host", List.of("localhost:8080"));
        answer.put("Connection", List.of("keep-alive"));
        answer.put("Accept", List.of("*/*"));
        assertThat(httpRequest.getHeaders().getValues()).isEqualTo(answer);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 body 부분을 추출한다")
    void getBodyTest() {
        byte[] answer = new byte[0];
        assertThat(httpRequest.getBody()).isInstanceOf(EmptyBody.class);
    }
}
