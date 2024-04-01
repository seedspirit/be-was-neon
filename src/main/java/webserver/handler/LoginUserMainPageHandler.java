package webserver.handler;

import db.SessionDatabase;
import model.User;

import webserver.URLConstants;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class LoginUserMainPageHandler extends CustomHtmlBuilder {
    private final String USER_NAME_BUTTON_CSS_ID = "authenticated-userNameButton";
    private final String USER_NAME_INSERTION_MARKER = "userNameText";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        try{
            User user = SessionDatabase.findUserBySessionId(sessionId);
            byte[] body = loadCustomHtml(
                    USER_NAME_BUTTON_CSS_ID,
                    USER_NAME_INSERTION_MARKER,
                    user.getUserId(),
                    requestLine.getRequestTarget());
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                    .body(body)
                    .build();
        } catch (NullPointerException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
                    .build();
        }
    }
}
