package webserver;

import db.Database;
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

    public void addUserInDatabase(String body){
        parseBody(body);
        String userID = userInfo.get(0);
        String password = userInfo.get(2);
        String name =  URLDecoder.decode(userInfo.get(1), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(userInfo.get(3), StandardCharsets.UTF_8);
        User user = new User(userID, password, name, email);
        Database.addUser(user);
        logger.debug("회원가입 성공! ID: {}, Name: {}", userID, name);
    }
}
