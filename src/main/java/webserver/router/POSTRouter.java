package webserver.router;

import db.SessionDatabase;
import webserver.handler.LoginHandler;
import webserver.session.Session;
import webserver.handler.TargetHandlerExtractor;
import webserver.handler.UserCreateHandler;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.NoSuchElementException;

import static webserver.httpMessage.HttpConstants.*;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.BAD_REQUEST;

public class POSTRouter implements Router {
    private final String DEFAULT_INDEX_PAGE = "/index.html";
    private final String LOGIN_FAILED_PAGE = "/login/login_failed.html";
    private final String LOGIN_USER_DEFAULT_INDEX_PAGE = "/user/index.html";
    private final String REGISTER_FAILED_PAGE = "/registration/register_failed.html";

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest.getRequestTarget());
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequest.getBody());
                    return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                            .contentType(ContentType.HTML)
                            .addHeaderComponent(LOCATION, DEFAULT_INDEX_PAGE)
                            .build();
                }
                case "/login" -> {
                    LoginHandler loginHandler = new LoginHandler();
                    Session session = loginHandler.issueSession(httpRequest.getBody());
                    return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                            .contentType(ContentType.HTML)
                            .addHeaderComponent(SET_COOKIE, session.toString())
                            .addHeaderComponent(LOCATION, LOGIN_USER_DEFAULT_INDEX_PAGE)
                            .build();
                }
                default -> {
                    return new HttpResponse.Builder(NOT_FOUND.getStatusCode(),
                            NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다").build();
                }
            }
        } catch (UrlFormatException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase() + e.getMessage())
                    .addHeaderComponent(LOCATION, REGISTER_FAILED_PAGE)
                    .build();
        } catch (NoSuchElementException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase() + e.getMessage())
                    .addHeaderComponent(LOCATION, LOGIN_FAILED_PAGE)
                    .build();
        }
    }
}
