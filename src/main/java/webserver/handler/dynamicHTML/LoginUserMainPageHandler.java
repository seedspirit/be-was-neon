package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import db.SessionDatabase;
import model.User;

import webserver.URLConstants;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.html.ExceptionHTMLGenerator;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

public class LoginUserMainPageHandler extends CustomHtmlBuilder {
    private final String USER_NAME_INSERTION_MARKER = "userNameText";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        try{
            User user = SessionDatabase.findUserBySessionId(sessionId);
            String basicHtml = loadHtml(requestLine.getRequestTarget());
            String customizedHtml = modifyHtmlBySelector(basicHtml, USER_NAME_INSERTION_MARKER, user.getUserId());
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                    .body(customizedHtml.getBytes())
                    .build();
        } catch (NullPointerException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
                    .build();
        } catch (ResourceNotFoundException e) {
            byte[] body = ExceptionHTMLGenerator.getHtml(NOT_FOUND).getBytes();
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .body(body)
                    .build();
        }
    }
}