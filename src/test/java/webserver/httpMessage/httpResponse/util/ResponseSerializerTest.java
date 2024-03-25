package webserver.httpMessage.httpResponse.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.httpMessage.ContentType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static webserver.httpMessage.HttpConstants.*;
import static webserver.httpMessage.HttpStatus.OK;

class ResponseSerializerTest {

    @DisplayName("Map 형태의 statusLine을 입력받아 HTTP 응답 메시지 형식에 맞게 byte[] 타입으로 직렬화한 값을 반환한다")
    @Test
    void serializeStatusLineTest() {
        Map<String, String> statusLine = setupStatusLineExample();
        String answerString = "HTTP/1.1 200 OK \r\n";
        ResponseSerializer serializer = new ResponseSerializer();
        assertThat(serializer.serializeStatusLine(statusLine)).isEqualTo(answerString.getBytes());
    }

    private Map<String, String> setupStatusLineExample(){
        Map<String, String> statusLineExample = new HashMap<>();
        statusLineExample.put(HTTP_VERSION_KEY, "1.1");
        statusLineExample.put(STATUS_CODE_KEY, String.valueOf(OK.getStatusCode()));
        statusLineExample.put(REASON_PHRASE, OK.getReasonPhrase());
        return statusLineExample;
    }

    @DisplayName("Map 형태의 headers와 Content Type을 입력받아 HTTP 응답 메시지 형식에 맞게 byte[] 타입으로 직렬화한 값을 반환한다")
    @Test
    void serializeHeadersTest() {
        Map<String, String> headers = setupHeaders();
        ContentType contentType = ContentType.HTML;

        byte[] serializedHeaders = new ResponseSerializer().serializeHeaders(contentType, headers);
        String deserializedHeaders = new String(serializedHeaders);

        assertThat(deserializedHeaders).contains("Host: localhost:8080\r\n", "Connection: keep-alive\r\n");
        assertThat(deserializedHeaders).contains(ContentType.generateHttpContentTypeHeaderOf(contentType));
    }

    private Map<String, String> setupHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Connection", "keep-alive");
        return headers;
    }
}