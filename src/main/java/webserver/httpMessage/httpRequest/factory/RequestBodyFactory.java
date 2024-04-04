package webserver.httpMessage.httpRequest.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;
import webserver.exceptions.ParsingException;
import webserver.httpMessage.httpRequest.*;
import webserver.httpMessage.httpRequest.body.*;

import java.io.BufferedInputStream;
import java.io.IOException;

public class RequestBodyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    // HttpRequest의 상태에 따라 알맞은 Body 객체 생성
    public RequestBody createRequestBodyFrom(BufferedInputStream bis, RequestHeaders headers) throws ParsingException {
        if (headers.containsContentType()){
            int contentLength = headers.getContentLength();
            if(headers.contentTypeEqualsFormURLEncoded()){
                // Content Type이 Form URL Encoded의 경우 body의 값을 form 형태로 파싱하여 가지고 있는 Body 객체 생성
                return new URLEncodedFormBody(getBytesFrom(bis, contentLength));
            } else if (headers.contentTypeEqualsMultipartForm()) {
                return new MultipartFormBody(getBytesFrom(bis, contentLength), headers.getMultiFormBoundary());
            }
            // 기본적으로 byte[] 형태의 body를 가지고 있는 Body 객체 생성
            return new BinaryBody(getBytesFrom(bis, contentLength));
        }
        // Content Type이 없는 경우 빈 배열을 지니는 Body 객체 생성
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
