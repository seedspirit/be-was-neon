package webserver.httpMessage.htttpRequest.requestParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;

import java.io.BufferedInputStream;
import java.io.IOException;

public class RequestBodyParser {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public byte[] getParseResultFrom(BufferedInputStream bis, int contentLength) {
        byte[] bytes = new byte[contentLength];
        try{
            bytes = Reader.readBodyFrom(bis, contentLength);
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
        return bytes;
    }
}
