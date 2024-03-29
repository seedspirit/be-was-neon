package webserver;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;

public class Authenticator {
    public boolean isAuthenticated(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        if(requestLine.requiresPublicAccessURL()){
            return true;
        };

        if(requestHeaders.isClientSessionAuthenticated()){
            return true;
        };

        return false;
    }
}
