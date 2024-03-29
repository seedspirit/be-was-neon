package webserver.handler;

import db.UserDatabase;
import model.User;
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
import static webserver.httpMessage.HttpStatus.FOUND;

class UserCreateHandlerTest {

    private UserCreateHandler handler;

    @BeforeEach
    void setUp(){
        this.handler = new UserCreateHandler();
        UserDatabase.clearDatabase();
    }


    @DisplayName("요청에 맞는 정보를 지닌 User 객체가 회원가입에 성공하여 데이터베이스에 저장된다")
    @Test
    void addUserInDatabaseTest() {
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

        HttpResponse httpResponse = sendRequestAndGetResponse(requestExample);
        User actualUser = UserDatabase.findUserById("javajigi");
        User expectedUser = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        assertThat(actualUser.toString()).isEqualTo(expectedUser.toString());
    }

    @DisplayName("잘못된 이메일 형식을 입력하였을 때 회원가입 실패 페이지로 리다이렉션하는 응답을 반환받는다")
    @Test
    void invalidEmailFormatTest() {
        String requestExample =
                """
                POST /create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 99
                Content-Type: application/x-www-form-urlencoded
                Accept: */*
               
                username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigislipp.net
                """;
        HttpResponse httpResponse = sendRequestAndGetResponse(requestExample);
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatusCode());
        assertThat(httpResponse.getReasonPhrase()).isEqualTo(FOUND.getReasonPhrase() + ": 잘못된 이메일 형식입니다");
        assertThat(httpResponse.getHeader().get(LOCATION)).isEqualTo("/registration/register_failed.html");
    }

    @DisplayName("잘못된 이름 형식을 입력하였을 때 회원가입 실패 페이지로 리다이렉션하는 응답을 반환받는다")
    @Test
    void invalidNameFormatTest() {
        String requestExample =
                """
                POST /create HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 99
                Content-Type: application/x-www-form-urlencoded
                Accept: */*
               
                username=javajigi&nickname=jae..sung&password=password&email=javajigi%40slipp.net
                """;
        HttpResponse httpResponse = sendRequestAndGetResponse(requestExample);
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatusCode());
        assertThat(httpResponse.getReasonPhrase()).isEqualTo(FOUND.getReasonPhrase() + ": 잘못된 이름 형식입니다");
        assertThat(httpResponse.getHeader().get(LOCATION)).isEqualTo("/registration/register_failed.html");
    }

    private HttpResponse sendRequestAndGetResponse(String requestExample) {
        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        RequestFactory requestFactory = new RequestFactory();
        HttpRequest httpRequest = requestFactory.createHttpRequestFrom(is);
        return handler.handleRequest(httpRequest);
    }
}