package db;

import model.User;

import java.util.*;

public class UserDatabase {
    private static Map<String, User> users = new LinkedHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static boolean isUserExists(String userId) {
        return findUserById(userId) != null;
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void clearDatabase(){
        users.clear();
    }

    public static String generateUserInfoTable() {
        StringBuilder result = new StringBuilder();
        for (User user : users.values()){
            result.append("<tr>\n")
                    .append(String.format("<td>%s</td>\n", user.getUserId()))
                    .append(String.format("<td>%s</td>\n", user.getName()))
                    .append("</tr>\n");
        }
        return result.toString();
    }
}