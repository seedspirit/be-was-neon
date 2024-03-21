package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exceptions.ResourceNotFoundException;
import webserver.handler.ResourceLoadHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ResourceLoadHandlerTest {
    private final String BASE_URL = "./src/main/resources/static";

    private ResourceLoadHandler resourceLoadHandler;

    @DisplayName("요청한 경로에 리소스가 없는 경우 ResourceNotFoundException 예외를 발생시킨다")
    @Test
    void loadExceptionTest() throws ResourceNotFoundException {
        String invalidPath = "./src/main/resources/static/inqeux.html";
        assertThatThrownBy(() -> new ResourceLoadHandler().load(invalidPath))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}