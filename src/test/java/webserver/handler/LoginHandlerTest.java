package webserver.handler;

import db.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.factory.RequestFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpConstants.SET_COOKIE;
import static webserver.httpMessage.HttpStatus.FOUND;

class LoginHandlerTest {

    private LoginHandler loginHandler;

    @BeforeEach
    void setLoginEnv(){
        UserDatabase.clearDatabase();
        registerTestUser();
        this.loginHandler = new LoginHandler();
    }

    void registerTestUser(){
        String registrationRequest =
                """
                POST /create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 99
                Content-Type: application/x-www-form-urlencoded
                Accept: */*
               
                username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigi%40slipp.net
                """;

        handleRequest(registrationRequest, new UserCreateHandler());
    }


    @DisplayName("로그인에 성공하면 쿠키와 함께 로그인 유저를 위한 index.html 페이지로 리다이레션하는 응답을 반환한다")
    @Test
    void handleRequestSuccessTest() {
        HttpResponse httpResponse = login("javajigi", "password");
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatusCode());
        assertThat(httpResponse.getHeader()).containsKeys(SET_COOKIE);
        assertThat(httpResponse.getHeader().get(LOCATION)).isEqualTo("/main/index.html");
    }

    @DisplayName("존재하지 않는 아이디로 로그인 요청을 보내면 로그인 실패 페이지로 리다이렉션하는 응답을 반환한다")
    @Test
    void invalidUserIdLoginTest() {
        HttpResponse httpResponse = login("java", "password");
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatusCode());
        assertThat(httpResponse.getHeader().get(LOCATION)).isEqualTo("/login/login_failed.html");
    }

    @DisplayName("틀린 비밀번호로 로그인 요청을 보내면 로그인 실패 페이지로 리다이렉션하는 응답을 반환한다")
    @Test
    void invalidPasswordLoginTest() {
        HttpResponse httpResponse = login("javajigi", "pass");
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatusCode());
        assertThat(httpResponse.getHeader().get(LOCATION)).isEqualTo("/login/login_failed.html");
    }

    private HttpResponse login(String username, String password){
        String loginRequestTemplate = """
            POST /login HTTP/1.1
            Host: localhost:8080
            Connection: keep-alive
            Content-Length: %d
            Content-Type: application/x-www-form-urlencoded
            Accept: */*

            username=%s&password=%s
            """;
        String requestBody = String.format("username=%s&password=%s", username, password);
        String loginRequest = String.format(loginRequestTemplate, requestBody.getBytes().length, username, password);
        return handleRequest(loginRequest, loginHandler);
    }

    private HttpResponse handleRequest(String request, Handler handler){
        HttpRequest httpRequest = makeRequestFrom(request);
        return handler.handleRequest(httpRequest);
    }

    private HttpRequest makeRequestFrom(String requestExample) {
        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        RequestFactory requestFactory = new RequestFactory();
        return requestFactory.createHttpRequestFrom(is);
    }
}