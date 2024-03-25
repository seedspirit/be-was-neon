package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static util.constants.Delimiter.*;
import static webserver.httpMessage.HttpConstants.*;

public class ResponseTransmitter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String UTF_8 = "utf-8";

    public void transmit(HttpResponse response, OutputStream outputStream) {
        DataOutputStream dos = new DataOutputStream(outputStream);
        byte[] responseLine = generateResponseLine(response.getStatusLine());
        byte[] header = generateHeader(response.getContentType(), response.getHeader());
        byte[] body = response.getBody();
        try {
            dos.write(responseLine);
            dos.write(header);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] generateResponseLine(Map<String, String> statusLine) {
        String template = "HTTP/%s %s %s %s";
        return String.format(template,
                statusLine.get(HTTP_VERSION_KEY),
                statusLine.get(STATUS_CODE_KEY),
                statusLine.get(REASON_PHRASE),
                CRLF)
                .getBytes();
    }

    private byte[] generateHeader(ContentType contentType, Map<String, String> header) {
        StringBuilder headerBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : header.entrySet()){
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
