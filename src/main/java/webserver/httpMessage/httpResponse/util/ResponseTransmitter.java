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
    private OutputStream out;

    public ResponseTransmitter(OutputStream out){
        this.out = out;
    }

    public void transmit(HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        ResponseSerializer serializer = new ResponseSerializer();
        byte[] responseLine = serializer.serializeStatusLine(response.getStatusLine());
        byte[] header = serializer.serializeHeaders(response.getHeader());
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
