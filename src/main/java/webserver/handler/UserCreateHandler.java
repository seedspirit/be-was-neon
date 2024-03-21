package webserver.handler;

import db.UserDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCreateHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private List<String> userInfo;
    private final String PARAM_MATCHING_PATTER = "(?<=userId=)[^&\\s]*|(?<=password=)[^&\\s]*|(?<=name=)[^&\\s]*|(?<=email=)[^&\\s]*";
    private final String NAME_VALIDATION_REGEX = "^[가-힣a-zA-Z\\s]+$";
    private final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public UserCreateHandler() {
        this.userInfo = new ArrayList<>();
    }

    private void parseBody(String body) {
        Pattern pattern = Pattern.compile(PARAM_MATCHING_PATTER);
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()) {
            userInfo.add(matcher.group());
        }
    }

    public void addUserInDatabase(String body) throws IllegalArgumentException {
        parseBody(body);
        String userID = userInfo.get(0);
        String password = userInfo.get(2);
        String name =  URLDecoder.decode(userInfo.get(1), StandardCharsets.UTF_8);
        if (!isValidNameFormat(name)){
            throw new IllegalArgumentException(": 잘못된 이름 형식입니다");
        }
        String email = URLDecoder.decode(userInfo.get(3), StandardCharsets.UTF_8);
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException(": 잘못된 이메일 형식입니다");
        }
        User user = new User(userID, password, name, email);
        UserDatabase.addUser(user);
        logger.debug("회원가입 성공! ID: {}, Name: {}", userID, name);
    }

    private boolean isValidNameFormat(String name){
        return name.matches(NAME_VALIDATION_REGEX);
    }

    private boolean isValidEmailFormat(String email){
        return email.matches(EMAIL_VALIDATION_REGEX);
    }
}
