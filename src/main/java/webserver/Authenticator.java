package webserver;

import db.SessionDatabase;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.htttpRequest.RequestLine;

import java.util.Optional;

public class Authenticator {
    public boolean isAuthenticated(HttpRequest httpRequest){
        Optional<String> loginCookie = httpRequest.getLoginCookie();
        RequestLine requestLine = httpRequest.getRequestLine();
        if(requestLine.requiresPublicAccessURL()){
            return true;
        };

        if(isClientLoginUser(loginCookie)){
            return true;
        };

        return false;
    }

    private boolean isClientLoginUser(Optional<String> loginCookie){
        return loginCookie.isPresent() && SessionDatabase.isSessionIdExists(loginCookie.get());
    }
}
