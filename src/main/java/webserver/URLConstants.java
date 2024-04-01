package webserver;

import java.util.List;
import java.util.regex.Pattern;

public class URLConstants {
    public static final String DEFAULT_INDEX_PAGE = "/index.html";
    public static final String LOGIN_USER_DEFAULT_INDEX_PAGE = "/main/index.html";
    public static final String LOGIN_INDEX_PAGE = "/login/index.html";
    public static final String LOGIN_FAILED_PAGE = "/login/login_failed.html";
    public static final String STATIC_DIR_PATH = "/static";
    public static final String REGISTRATION_FAILED_PAGE = "/registration/register_failed.html";
    public static final String ARTICLE_DIRECTORY = "/article.*";
    public static final String COMMENT_DIRECTORY = "/comment.*";
    public static final String USER_DIR = "/user/.*";
    public static final String USER_LIST_PAGE = "/user/list.html";

    public enum AccessLevel {
        PRIVATE(List.of(LOGIN_USER_DEFAULT_INDEX_PAGE, ARTICLE_DIRECTORY, COMMENT_DIRECTORY, USER_DIR));

        private final List<String> urls;

        AccessLevel(List<String> urls) {
            this.urls = urls;
        }

        public static boolean belongsToPublic(String url){
            return PRIVATE.urls.stream().noneMatch(u -> Pattern.compile(u).matcher(url).matches());
        }
    }
}
