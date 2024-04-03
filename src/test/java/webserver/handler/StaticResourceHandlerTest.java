package webserver.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.ParsingException;
import webserver.exceptions.ResourceNotFoundException;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.factory.RequestFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

class StaticResourceHandlerTest {

    @DisplayName("요청한 경로에 리소스가 없는 경우 404 응답을 반환한다")
    @Test
    void loadExceptionTest() throws ResourceNotFoundException, ParsingException {
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
        RequestFactory requestFactory = new RequestFactory(is);
        HttpRequest httpRequest = requestFactory.createHttpRequest();
        HttpResponse actualResponse = new StaticResourceHandler().handleRequest(httpRequest);
        assertThat(actualResponse.getStatusCode()).isEqualTo(NOT_FOUND.getStatusCode());
        assertThat(actualResponse.getReasonPhrase()).isEqualTo(NOT_FOUND.getReasonPhrase());
    }
}