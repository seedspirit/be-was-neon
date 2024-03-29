package webserver.httpMessage.htttpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.httpMessage.htttpRequest.*;
import webserver.httpMessage.htttpRequest.body.BinaryBody;
import webserver.httpMessage.htttpRequest.body.EmptyBody;
import webserver.httpMessage.htttpRequest.body.FormBody;
import webserver.httpMessage.htttpRequest.body.RequestBody;

import java.io.BufferedInputStream;
import java.io.IOException;

public class RequestBodyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public RequestBody createRequestBodyFrom(BufferedInputStream bis, RequestHeaders headers) {
        if (headers.containsContentType()){
            int contentLength = headers.getContentLength();
            if(headers.contentTypeEqualsFormURLEncoded()){
                return new FormBody(getBytesFrom(bis, contentLength));
            }
            return new BinaryBody(getBytesFrom(bis, contentLength));
        }
        return new EmptyBody();
    }

    private byte[] getBytesFrom(BufferedInputStream bis, int contentLength) {
        byte[] bytes = new byte[contentLength];
        try{
            bytes = Reader.readBodyFrom(bis, contentLength);
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
        return bytes;
    }
}
