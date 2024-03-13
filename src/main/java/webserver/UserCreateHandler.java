package webserver;

import db.Database;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCreateHandler {
    private List<String> userInfo;
    private final String PARAM_MATCHING_PATTER = "(?<=userId=)[^&\\s]*|(?<=password=)[^&\\s]*|(?<=name=)[^&\\s]*|(?<=email=)[^&\\s]*";

    public UserCreateHandler(String requestTarget) {
        this.userInfo = new ArrayList<>();
        parseMsg(requestTarget);
    }

    public void parseMsg(String requestTarget) {
        Pattern pattern = Pattern.compile(PARAM_MATCHING_PATTER);
        Matcher matcher = pattern.matcher(requestTarget);
        while (matcher.find()) {
            userInfo.add(matcher.group());
        }
    }

    public void addUserInDatabase(){
        String userID = userInfo.get(0);
        String password = userInfo.get(2);
        String name =  URLDecoder.decode(userInfo.get(1), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(userInfo.get(3), StandardCharsets.UTF_8);
        User user = new User(userID, password, name, email);
        Database.addUser(user);
    }
}
