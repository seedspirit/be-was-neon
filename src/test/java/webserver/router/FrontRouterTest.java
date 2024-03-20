package webserver.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.Reader;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.router.FrontRouter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpStatus.METHOD_NOT_ALLOWED;

class FrontRouterTest {

    private FrontRouter frontRouter;

    @BeforeEach
    void setRouter(){
        this.frontRouter = new FrontRouter();
    }

    @DisplayName("각 요청에 맞는 HttpResponseMsg를 반환한다")
    @ParameterizedTest(name = "{index}) statusCode: {1}, reasonPhrase: {2}")
    @CsvSource({
            "/index.html, 200, OK",
            "/index.html, 200, OK",
            "/registration, 200, OK",
            "/mission, 404, Not Found: 요청한 리소스를 찾을 수 없습니다",
            "\\dkjwn.dj, 400, Bad Request: 잘못된 형식의 URL입니다"
    })
    void routeTest(String requestPath, int expectedStatusCode, String expectedReasonPhrase) {
        String requestTemplate =
                """
                GET %s HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Accept:
                """;

        String requestExample = String.format(requestTemplate, requestPath);

        HttpResponse actualResponse = sendRequestAndGetResponse(requestExample);
        HttpResponse expectedResponse = generateExpectedResponse(expectedStatusCode, expectedReasonPhrase);

        assertThat(actualResponse.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
        assertThat(actualResponse.getReasonPhrase()).isEqualTo(expectedResponse.getReasonPhrase());
    }

    @DisplayName("지원하지 않는 메서드로 요청할 경우 405 에러를 반환한다")
    @Test
    void methodTest(){
        String requestExample =
                """
                DELETE /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                Accept:
                """;

        HttpResponse actualResponseMsg = sendRequestAndGetResponse(requestExample);
        HttpResponse expectedResponseMsg = generateExpectedResponse(METHOD_NOT_ALLOWED.getStatusCode(), METHOD_NOT_ALLOWED.getReasonPhrase());

        assertThat(actualResponseMsg.getStatusCode()).isEqualTo(expectedResponseMsg.getStatusCode());
        assertThat(actualResponseMsg.getReasonPhrase()).isEqualTo(expectedResponseMsg.getReasonPhrase());
    }

    private BufferedReader createBufferedReaderFromString(String request) {
        InputStream is = new ByteArrayInputStream(request.getBytes());
        return new BufferedReader(new InputStreamReader(is));
    }

    private HttpResponse generateExpectedResponse(int statusCode, String reasonPhrase) {
        return new HttpResponse.Builder(statusCode, reasonPhrase).build();
    }

    private HttpResponse sendRequestAndGetResponse(String requestExample) {
        BufferedReader br = createBufferedReaderFromString(requestExample);
        HttpRequest httpRequest = new HttpRequest(br);
        return frontRouter.route(httpRequest);
    }
}
