package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Reader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        BufferedReader br = Reader.inputStreamToBufferedReader(is);
        httpRequest = new HttpRequest(br);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 method인 GET을 추출한다")
    void getMethodTest() {
        assertThat(httpRequest.getMethod()).isEqualTo("GET");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 request target인 \"/index.html\"을 추출한다")
    void getRequestTargetTest() {
        assertThat(httpRequest.getRequestTarget()).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 header를 key-value 형태로 추출한다")
    void getHeadersTest() {
        Map<String, List<String>> answer = new HashMap<>();
        answer.put("Host", List.of("localhost:8080"));
        answer.put("Connection", List.of("keep-alive"));
        answer.put("Accept", List.of("*/*"));
        assertThat(httpRequest.getHeaders()).isEqualTo(answer);
    }

    @Test
    @DisplayName("예시 http요청 메시지 중 body 부분을 추출한다")
    void getBodyTest() {
        String answer = "";
        assertThat(httpRequest.getBody()).isEqualTo(answer);
    }
}
