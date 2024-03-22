package webserver.handler;

import db.SessionDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.Optional;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;

public class LogoutHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String DEFAULT_INDEX_PAGE = "/index.html";

    public HttpResponse handleRequest(HttpRequest httpRequest){
        Optional<String> loginCookie = httpRequest.getLoginCookie();
        if(loginCookie.isPresent()){
            logger.debug("로그아웃 성공! Name: {}, SessionId: {}"
                    , SessionDatabase.findUserBySessionId(loginCookie.get()).getName(), loginCookie.get());
            loginCookie.ifPresent(SessionDatabase::removeRecordOf);
        }
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .contentType(ContentType.HTML)
                .addHeaderComponent(LOCATION, DEFAULT_INDEX_PAGE)
                .build();
    }
}
