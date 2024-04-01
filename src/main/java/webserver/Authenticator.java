package webserver;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;

public class Authenticator {
    public boolean isAuthenticated(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        // 인가가 필요하지 않은 페이지에 접근하는 경우 true
        if(requestLine.requiresPublicAccessURL()){
            return true;
        };

        // 로그인 한 이후 인가가 필요한 페이지에 접근하는 경우 true
        if(requestHeaders.isClientSessionAuthenticated()){
            return true;
        };

        // 로그인 하지 않았으나 인가가 필요한 페이지에 접근하는 경우 false
        return false;
    }
}
