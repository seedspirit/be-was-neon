package webserver.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.ResourceNotFoundException;
import webserver.handler.ResourceLoadHandler;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

class ResourceLoadHandlerTest {

    @DisplayName("요청한 경로에 리소스가 없는 경우 404 응답을 반환한다")
    @Test
    void loadExceptionTest() throws ResourceNotFoundException {
        String requestExample =
                """
                GET /inqeux.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Content-Length: 0
                Content-Type: text/html
                Accept: */*
               
                """;
        InputStream is = new ByteArrayInputStream(requestExample.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        HttpRequest httpRequest = new HttpRequest(br);
        HttpResponse actualResponse = new ResourceLoadHandler().handleRequest(httpRequest);
        assertThat(actualResponse.getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
        assertThat(actualResponse.getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다");
    }
}