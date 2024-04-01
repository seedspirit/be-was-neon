package webserver.httpMessage.httpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.exceptions.ParsingException;
import webserver.httpMessage.httpRequest.*;
import webserver.httpMessage.httpRequest.body.BinaryBody;
import webserver.httpMessage.httpRequest.body.EmptyBody;
import webserver.httpMessage.httpRequest.body.FormBody;
import webserver.httpMessage.httpRequest.body.RequestBody;

import java.io.BufferedInputStream;
import java.io.IOException;

public class RequestBodyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public RequestBody createRequestBodyFrom(BufferedInputStream bis, RequestHeaders headers) throws ParsingException {
        if (headers.containsContentType()){
            int contentLength = headers.getContentLength();
            if(headers.contentTypeEqualsFormURLEncoded()){
                return new FormBody(getBytesFrom(bis, contentLength));
            }
            return new BinaryBody(getBytesFrom(bis, contentLength));
        }
        return new EmptyBody();
    }

    private byte[] getBytesFrom(BufferedInputStream bis, int contentLength) throws ParsingException {
        byte[] bytes = new byte[contentLength];
        try{
            bytes = Reader.readBodyFrom(bis, contentLength);
            return bytes;
        } catch (IOException e) {
           logger.error(e.getMessage());
           throw new ParsingException();
        }
    }
}
