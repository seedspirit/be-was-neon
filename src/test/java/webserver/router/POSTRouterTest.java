package webserver.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

class POSTRouterTest {

    private POSTRouter postRouter;

    @BeforeEach
    void setRouter(){
        this.postRouter = new POSTRouter();
    }

    @DisplayName("회원가입이 성공한 경우 http://localhost:8080/index.html로 리다이렉트하는 응답을 반환한다")
    @Test
    void redirectTest() {
        String requestExample =
                """
                POST /create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 99
                Content-Type: application/x-www-form-urlencoded
                Accept: */*
               
                username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigi%40slipp.net
                """;

        HttpResponse actualResponseMsg = sendRequestAndGetResponse(requestExample);;
        final String redirectLocation = "http://localhost:8080/index.html";
        HttpResponse expectedResponseMsg = new HttpResponse
                .Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .addHeaderComponent(LOCATION, redirectLocation)
                .build();

        verify(actualResponseMsg, expectedResponseMsg);
    }


    @DisplayName("지원하지 않는 url을 입력한 경우 404 에러 응답을 반환한다")
    @Test
    void notFoundResponseTest() {
        String requestExample =
                """
                POST /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Accept:
                
                examplebody
                """;

        HttpResponse actualResponseMsg = sendRequestAndGetResponse(requestExample);;
        HttpResponse expectedResponseMsg = generateExpectedResponse(NOT_FOUND.getStatusCode(),
                NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다");

        verify(actualResponseMsg, expectedResponseMsg);
    }

    private void verify(HttpResponse actual, HttpResponse expected){
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getReasonPhrase()).isEqualTo(expected.getReasonPhrase());
    }

    private HttpResponse sendRequestAndGetResponse(String requestExample) {
        BufferedReader br = createBufferedReaderFromString(requestExample);
        HttpRequest httpRequest = new HttpRequest(br);
        return postRouter.route(httpRequest);
    }

    private BufferedReader createBufferedReaderFromString(String request) {
        InputStream is = new ByteArrayInputStream(request.getBytes());
        return new BufferedReader(new InputStreamReader(is));
    }

    private HttpResponse generateExpectedResponse(int statusCode, String reasonPhrase) {
        return new HttpResponse.Builder(statusCode, reasonPhrase).build();
    }

}