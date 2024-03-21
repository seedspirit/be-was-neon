package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.UrlFormatException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetHandlerExtractorTest {

    @DisplayName("요청 메시지의 url에서 첫번째 / 구간을 추출한다")
    @Test
    void extractionTest() throws UrlFormatException {
        String expected = "/create";
        String urlExample = "/create?nickname=nickname&password=password";
        TargetHandlerExtractor extractor = new TargetHandlerExtractor();
        String actual = extractor.extractTargetHandler(urlExample);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("잘못된 형태의 url에서 첫번째 / 구간을 추출한다")
    @Test
    void errorMakingTest() {
        String urlExample = "+/registration";
        TargetHandlerExtractor extractor = new TargetHandlerExtractor();
        assertThatThrownBy(() -> extractor.extractTargetHandler(urlExample))
                .isInstanceOf(UrlFormatException.class);
    }
}