package webserver.handler;

import db.SessionDatabase;
import webserver.httpMessage.HttpRequest;

import java.util.Optional;

public class LogoutHandler {
    public void logout(HttpRequest httpRequest){
        Optional<String> loginCookie = httpRequest.getLoginCookie();
        loginCookie.ifPresent(SessionDatabase::removeRecordOf);
    }
}
