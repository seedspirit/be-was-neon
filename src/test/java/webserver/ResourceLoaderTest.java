package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.ResourceNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ResourceLoaderTest {
    private final String BASE_URL = "./src/main/resources/static";

    private ResourceLoader resourceLoader;

    @DisplayName("요청한 경로에 리소스가 없는 경우 ResourceNotFoundException 예외를 발생시킨다")
    @Test
    void loadExceptionTest() throws ResourceNotFoundException {
        String invalidPath = "./src/main/resources/static/inqeux.html";
        assertThatThrownBy(() -> new ResourceLoader().load(invalidPath))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}