package webserver.handler;

import db.UserDatabase;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.htttpRequest.FormBody;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.RequestBody;
import webserver.session.Cookie;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static util.constants.Delimiter.AMPERSAND;
import static util.constants.Delimiter.EQUAL_SIGN;
import static webserver.URLConstants.LOGIN_FAILED_PAGE;
import static webserver.URLConstants.LOGIN_USER_DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpConstants.SET_COOKIE;
import static webserver.httpMessage.HttpStatus.FOUND;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);
    private final String USERID_PARAM = "username";
    private final String PASSWORD_PARAM = "password";
    private Cookie cookie;

    public HttpResponse handleRequest(HttpRequest httpRequest){
        if(isLoginSucceed(httpRequest.getBody())){
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .addHeaderComponent(SET_COOKIE, cookie.toString())
                    .addHeaderComponent(LOCATION, LOGIN_USER_DEFAULT_INDEX_PAGE)
                    .build();
        }
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .addHeaderComponent(LOCATION, LOGIN_FAILED_PAGE)
                .build();
    }

    private boolean isLoginSucceed(RequestBody body) {
        FormBody formBody = (FormBody) body;
        Map<String, String> params = formBody.getFormParsedBytes();
        String userId = params.get(USERID_PARAM);
        if(!isUserExistsInDB(userId)){
            return false;
        }

        User user = UserDatabase.findUserById(userId);
        String passwordInput = params.get(PASSWORD_PARAM);
        if (!isPasswordInputCorrect(user, passwordInput)){
            return false;
        }

        this.cookie = new Cookie();
        registerCookieUserPair(cookie, user);
        logger.debug("로그인 성공! Name: {}, SessionId: {}", user.getName(), cookie.getSessionId());
        return true;
    }

    private boolean isPasswordInputCorrect(User user, String passwordInput) {
        return user.getPassword().equals(passwordInput);
    }

    private boolean isUserExistsInDB(String userId) {
        return UserDatabase.isUserExists(userId);
    }

    private void registerCookieUserPair(Cookie cookie, User user){
        SessionDatabase.addRecord(cookie, user);
    }
}
