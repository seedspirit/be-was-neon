package webserver.handler;

import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.RequestHeaders;
import webserver.session.Cookie;

import static webserver.URLConstants.DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpConstants.SET_COOKIE;
import static webserver.httpMessage.HttpStatus.FOUND;

public class LogoutHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        if(requestHeaders.isClientSessionAuthenticated()){
            String sessionId = requestHeaders.getLoginCookieSessionId();
            Pair<Cookie, User> cookieUserPair = SessionDatabase.findCookieUserPairBySessionId(sessionId);
            Cookie cookie = cookieUserPair.getCookie();
            User user = cookieUserPair.getUser();
            executeLogout(cookie);
            logger.debug("로그아웃 성공! Name: {}, SessionId: {}", user.getName(), sessionId);

            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .addHeaderComponent(SET_COOKIE, cookie.toString())
                    .addHeaderComponent(LOCATION, DEFAULT_INDEX_PAGE)
                    .build();
        }
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .contentType(ContentType.HTML)
                .addHeaderComponent(LOCATION, DEFAULT_INDEX_PAGE)
                .build();
    }

    private void executeLogout(Cookie cookie){
        cookie.invalidateCookie();
        SessionDatabase.removeRecordOf(cookie.getSessionId());
    }
}
