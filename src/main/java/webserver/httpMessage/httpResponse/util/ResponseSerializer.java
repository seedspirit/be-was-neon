package webserver.httpMessage.httpResponse.util;

import webserver.httpMessage.ContentType;

import java.util.Map;

import static util.constants.Delimiter.*;
import static util.constants.Delimiter.BLANK;
import static webserver.httpMessage.HttpConstants.*;

public class ResponseSerializer {

    public byte[] serializeStatusLine(Map<String, String> statusLine){
        String template = "HTTP/%s %s %s %s";
        return String.format(template,
                        statusLine.get(HTTP_VERSION_KEY),
                        statusLine.get(STATUS_CODE_KEY),
                        statusLine.get(REASON_PHRASE),
                        CRLF)
                .getBytes();
    }

    public byte[] serializeHeaders(Map<String, String> headers) {
        StringBuilder headerBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : headers.entrySet()){
            headerBuilder.append(generateOneHeaderLine(entry.getKey(), entry.getValue()));
        }
        String contentTypeLine = buildContentTypeHeaderLine(headers.get(CONTENT_TYPE));
        headerBuilder.append(contentTypeLine);
        headerBuilder.append(CRLF);
        return headerBuilder.toString().getBytes();
    }

    private String buildContentTypeHeaderLine(String mimeType) {
        return generateOneHeaderLine(CONTENT_TYPE, ContentType.generateHttpContentTypeHeaderOf(mimeType));
    }

    private String generateOneHeaderLine(String headerName, String headerValue) {
        return headerName + COLON + BLANK + headerValue + CRLF;
    }
}
