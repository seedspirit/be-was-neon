package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.exceptions.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ResourceLoaderTest {
    private final String BASE_URL = "./src/main/resources/static";

    private ResourceLoader resourceLoader;

    @BeforeEach
    void setResourceLoader() {
        this.resourceLoader = new ResourceLoader();
    }

    @DisplayName("요청한 경로에 있는 파일을 읽어 string으로 변환한다")
    @ParameterizedTest(name = "{index}) 경로: {0}")
    @CsvSource({
            "/registration/register.html, 아이디를 입력해주세요",
            "/index.html, 우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며"
    } )
    void loadTest(String path, String content) throws ResourceNotFoundException {
        String loadResult = resourceLoader.load(BASE_URL + path);
        assertThat(loadResult.contains(content)).isTrue();
    }

    @DisplayName("요청한 경로에 리소스가 없는 경우 ResourceNotFoundException 예외를 발생시킨다")
    @Test
    void loadExceptionTest() throws ResourceNotFoundException {
        String invalidPath = "./src/main/resources/static/inqeux.html";
        assertThatThrownBy(() -> resourceLoader.load(invalidPath))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}