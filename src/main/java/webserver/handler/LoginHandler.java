package webserver.handler;

import db.UserDatabase;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.body.FormBody;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.body.RequestBody;
import webserver.session.Cookie;

import static webserver.URLConstants.LOGIN_FAILED_PAGE;
import static webserver.URLConstants.LOGIN_USER_DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpConstants.SET_COOKIE;
import static webserver.httpMessage.HttpStatus.FOUND;

public class LoginHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);
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
        // 유저 ID가 DB에 존재하는지 검증
        if(!formBody.userIdExistsInDB()){
            return false;
        }

        // 입력한 비밀번호와 로그인하고자 하는 User 객체의 비밀번호가 일치하는지 검증
        User user = UserDatabase.findUserById(formBody.getUserId());
        if (!formBody.passwordInputCorrespondPasswordOf(user)){
            return false;
        }

        // 세션ID 발급 후 세션ID와 유저를 Session DB에 저장
        this.cookie = new Cookie();
        registerCookieUserPair(cookie, user);
        logger.debug("로그인 성공! Name: {}, SessionId: {}", user.getName(), cookie.getSessionId());
        return true;
    }

    private void registerCookieUserPair(Cookie cookie, User user){
        SessionDatabase.addRecord(cookie, user);
    }
}
