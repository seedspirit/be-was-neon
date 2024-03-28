package webserver.httpMessage.htttpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.htttpRequest.*;
import webserver.httpMessage.htttpRequest.body.BinaryBody;
import webserver.httpMessage.htttpRequest.body.EmptyBody;
import webserver.httpMessage.htttpRequest.body.FormBody;
import webserver.httpMessage.htttpRequest.body.RequestBody;

import java.io.BufferedInputStream;
import java.io.IOException;

import static webserver.httpMessage.HttpConstants.CONTENT_LENGTH;
import static webserver.httpMessage.HttpConstants.CONTENT_TYPE;

public class RequestBodyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public RequestBody createRequestBodyFrom(BufferedInputStream bis, RequestHeaders headers) {
        if (headers.containsKey(CONTENT_TYPE)){
            int contentLength = Integer.parseInt(headers.getValueOf(CONTENT_LENGTH).get(0));
            if(headers.getValueOf(CONTENT_TYPE).get(0).equals(ContentType.FORM_URL_ENCODED.getMimetype())){
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
