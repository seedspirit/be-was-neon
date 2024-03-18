package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.router.Router;

import static org.assertj.core.api.Assertions.assertThat;

class RouterTest {

    private Router router;

    @BeforeEach
    void setRouter(){
        this.router = new Router();
    }

    @DisplayName("각 요청에 맞는 HttpResponseMsg를 반환한다")
    @ParameterizedTest(name = "{index}) statusCode: {1}, reasonPhrase: {2}")
    @CsvSource({
            "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net, 200, OK",
            "/index.html, 200, OK",
            "/register.html, 200, OK",
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
                Accept: */*
                """;

        String requestExample = String.format(requestTemplate, requestPath);
        HttpRequest httpRequest = new HttpRequest(requestExample);

        HttpResponse actualResponseMsg = router.route(httpRequest);
        HttpResponse expectedResponseMsg = new HttpResponse(expectedStatusCode, expectedReasonPhrase);

        assertThat(actualResponseMsg.getStatusCode()).isEqualTo(expectedResponseMsg.getStatusCode());
        assertThat(actualResponseMsg.getReasonPhrase()).isEqualTo(expectedResponseMsg.getReasonPhrase());
    }
}
