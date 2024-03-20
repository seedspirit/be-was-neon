package webserver.router;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static webserver.httpMessage.HttpStatus.METHOD_NOT_ALLOWED;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

class POSTRouterTest {

    @DisplayName("지원하지 않는 url을 입력한 경우 404 에러 응답을 반환한다")
    @Test
    void route() {
        String requestExample =
                """
                POST /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Accept:
                
                examplebody
                """;

        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        POSTRouter postRouter = new POSTRouter();
        HttpResponse actualResponseMsg = postRouter.route(new HttpRequest(br));
        HttpResponse expectedResponseMsg = generateExpectedResponse(NOT_FOUND.getStatusCode(),
                NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다");

        assertThat(actualResponseMsg.getStatusCode()).isEqualTo(expectedResponseMsg.getStatusCode());
        assertThat(actualResponseMsg.getReasonPhrase()).isEqualTo(expectedResponseMsg.getReasonPhrase());
    }

    private HttpResponse generateExpectedResponse(int statusCode, String reasonPhrase) {
        return new HttpResponse.Builder(statusCode, reasonPhrase).build();
    }

}