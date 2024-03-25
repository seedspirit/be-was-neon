package webserver.router;

import db.UserDatabase;
import org.junit.jupiter.api.*;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.RequestFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class POSTRouterTest {

    private POSTRouter postRouter;

    @BeforeEach
    void setRouter(){
        this.postRouter = new POSTRouter();
        UserDatabase.clearDatabase();
    }

    @DisplayName("존재하지 않는 회원으로 로그인을 시도하는 경우 302 응답을 반환한다")
    @Test
    @Order(1)
    void loginWithNotRegisteredIdTest() {
        String requestExample =
                """
                POST /login HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 35
                Content-Type: application/x-www-form-urlencoded
                Accept: */*
                
                username=java&password=password
                """;

        HttpResponse actualResponseMsg = sendRequestAndGetResponse(requestExample);;
        HttpResponse expectedResponseMsg = generateExpectedResponse(FOUND.getStatusCode(),
                FOUND.getReasonPhrase());

        verify(actualResponseMsg, expectedResponseMsg);
    }

    @DisplayName("회원가입이 성공한 경우 http://localhost:8080/index.html로 리다이렉트하는 응답을 반환한다")
    @Test
    @Order(2)
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
    @Order(3)
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
                NOT_FOUND.getReasonPhrase());

        verify(actualResponseMsg, expectedResponseMsg);
    }

    private void verify(HttpResponse actual, HttpResponse expected){
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getReasonPhrase()).isEqualTo(expected.getReasonPhrase());
    }

    private HttpResponse sendRequestAndGetResponse(String requestExample) {
        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        RequestFactory requestFactory = new RequestFactory();
        HttpRequest httpRequest = requestFactory.createHttpRequestFrom(is);
        return postRouter.route(httpRequest);
    }

    private HttpResponse generateExpectedResponse(int statusCode, String reasonPhrase) {
        return new HttpResponse.Builder(statusCode, reasonPhrase).build();
    }

}