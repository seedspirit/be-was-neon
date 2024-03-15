package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathRedirectorTest {

    @DisplayName("경로를 입력할 경우 규칙에 맞게 경로 보정하여 반환한다")
    @ParameterizedTest(name="{index}) 원래 경로: {0}, 보정 경로: {1}")
    @CsvSource({
            "/registration, ./src/main/resources/static/registration/register.html",
            "/index.html, ./src/main/resources/static/index.html",
            "/register.html, ./src/main/resources/static/registration/register.html"
    })
    void makeRedirectionTest(String originPath, String expectedRedirected) {
        PathRedirector pathRedirector = new PathRedirector();
        assertThat(pathRedirector.makeRedirection(originPath)).isEqualTo(expectedRedirected);
    }
}