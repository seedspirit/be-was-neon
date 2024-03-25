package webserver.httpMessage.httpResponse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseTransmitter {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public void transmit(HttpResponse response, OutputStream outputStream) {
        DataOutputStream dos = new DataOutputStream(outputStream);
        ResponseSerializer serializer = new ResponseSerializer();
        byte[] responseLine = serializer.serializeStatusLine(response.getStatusLine());
        byte[] header = serializer.serializeHeaders(response.getContentType(), response.getHeader());
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
}
