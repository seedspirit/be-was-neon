package webserver.handler.dynamicHTML;

import db.SessionDatabase;
import model.User;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;


public class LoginUserMainPageHandler extends CustomHtmlBuilder {
    private final String USER_NAME_INSERTION_MARKER = "userNameText";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        User user = SessionDatabase.findUserBySessionId(sessionId);
        return generateCustomHTML(
                requestLine.getRequestTarget(),
                USER_NAME_INSERTION_MARKER,
                user.getUserId());
    }
}