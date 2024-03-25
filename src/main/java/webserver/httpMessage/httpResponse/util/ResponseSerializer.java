package webserver.httpMessage.httpResponse.util;

import webserver.httpMessage.ContentType;

import java.util.Map;

import static util.constants.Delimiter.*;
import static util.constants.Delimiter.BLANK;
import static webserver.httpMessage.HttpConstants.*;

public class ResponseSerializer {
    private final String UTF_8 = "utf-8";

    public byte[] serializeStatusLine(Map<String, String> statusLine){
        String template = "HTTP/%s %s %s %s";
        return String.format(template,
                        statusLine.get(HTTP_VERSION_KEY),
                        statusLine.get(STATUS_CODE_KEY),
                        statusLine.get(REASON_PHRASE),
                        CRLF)
                .getBytes();
    }

    public byte[] serializeHeaders(ContentType contentType, Map<String, String> headers) {
        StringBuilder headerBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : headers.entrySet()){
            headerBuilder.append(generateOneHeaderLine(entry.getKey(), entry.getValue()));
        }
        String contentTypeLine = buildContentTypeHeaderLine(contentType);
        headerBuilder.append(contentTypeLine);
        headerBuilder.append(CRLF);
        return headerBuilder.toString().getBytes();
    }

    private String buildContentTypeHeaderLine(ContentType responseContentType) {
        if(!responseContentType.equals(ContentType.NONE)){
            if(responseContentType.getMimetype().contains("text")){
                return generateOneHeaderLine(CONTENT_TYPE, responseContentType.getMimetype() + SEMICOLON + MIME_TYPE_PARAMETER_CHARSET + EQUAL_SIGN + UTF_8);
            } else {
                return generateOneHeaderLine(CONTENT_TYPE, responseContentType.getMimetype());
            }
        }
        return generateOneHeaderLine(CONTENT_TYPE, EMPTY);
    }

    private String generateOneHeaderLine(String headerName, String headerValue) {
        return headerName + COLON + BLANK + headerValue + CRLF;
    }
}
