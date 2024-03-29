package webserver;

import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.htttpRequest.RequestHeaders;
import webserver.httpMessage.htttpRequest.RequestLine;

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
