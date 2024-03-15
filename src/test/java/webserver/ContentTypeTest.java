package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    static Stream<Arguments> requestLineAndContentType() {
        return Stream.of(
                Arguments.arguments("GET /index.html HTTP/1.1", ContentType.HTML),
                Arguments.arguments("GET /reset.csss HTTP/1.1", ContentType.CSS),
                Arguments.arguments("GET /registration HTTP/1.1", ContentType.NONE)
        );
    }

    @DisplayName("HTTP 요청 메시지 중 request line에서 콘텐츠 반환을 요청하는 경우, 그에 맞는 콘텐츠 타입을 반환한다")
    @ParameterizedTest
    @MethodSource("requestLineAndContentType")
    void findMatchingContentTypeTest(String requestLine, ContentType contentType) {
        assertThat(ContentType.findMatchingContentType(requestLine)).isEqualTo(contentType);
    }
}