package webserver;

import db.SessionDatabase;
import webserver.httpMessage.htttpRequest.HttpRequest;

import java.util.Map;
import java.util.Optional;

import static webserver.httpMessage.HttpConstants.REQUEST_TARGET;

public class Authenticator {
    public boolean isAuthenticated(HttpRequest httpRequest){
        Optional<String> loginCookie = httpRequest.getLoginCookie();
        if(isRequirePublicAccessURL(httpRequest.getRequestLine())){
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

    private boolean isRequirePublicAccessURL(Map<String, String> requestLine){
        return URLConstants.AccessLevel.belongsToPublic(requestLine.get(REQUEST_TARGET));
    }
}
