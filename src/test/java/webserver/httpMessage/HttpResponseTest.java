package webserver.httpMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpStatus.OK;

class HttpResponseTest {

    @Test
    @DisplayName("응답 메시지 객체 생성시 키-밸류 형태로 헤더에 원하는 값을 넣을 수 있다")
    void addHeaderComponentTest(){
        HttpResponse httpResponse = new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                .addHeaderComponent("Location", "/index.html")
                .build();

        assertThat(httpResponse.getHeader()).containsKeys("Location");
        assertThat(httpResponse.getHeader().get("Location")).isEqualTo("/index.html");
    }
}
