package webserver;

import db.Database;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static util.constants.Delimiter.EQUAL_SIGN;

public class LoginHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Map<String, String> params;

    public LoginHandler(){
        this.params = new HashMap<>();
    }

    public Session issueSession(String body) throws NoSuchElementException {
        parseBody(body);
        User user = findUserByUserId(params.get("username"));
        Session session = new Session();
        registerSessionUserPair(session, user);
        logger.debug("로그인 성공! Name: {}, SessionId: {}", user.getName(), session.getSessionId());
        return session;
    }

    private void parseBody(String body) {
        String[] parameters = body.split("&");
        for (String parameter : parameters) {
            String[] tmp = parameter.split(EQUAL_SIGN);
            String key = tmp[0];
            String value = URLDecoder.decode(tmp[1], StandardCharsets.UTF_8);
            params.put(key, value);
        }
    }

    private User findUserByUserId(String userID) throws NoSuchElementException {
        Optional<User> userOptional = Optional.ofNullable(Database.findUserById(userID));
        if (userOptional.isEmpty()){
            throw new NoSuchElementException(": 존재하지 않는 회원입니다");
        }
        return userOptional.get();
    }

    private void registerSessionUserPair(Session session, User user){
        SessionDatabase.addSession(session, user);
    }
}
