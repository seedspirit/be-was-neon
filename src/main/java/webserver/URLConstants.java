package webserver;

import java.util.List;
import java.util.regex.Pattern;

public class URLConstants {
    public static final String DEFAULT_INDEX_PAGE = "/index.html";
    public static final String LOGIN_USER_DEFAULT_INDEX_PAGE = "/main/index.html";
    public static final String MAIN_DIRECTORY = "/main/.*";
    public static final String LOGIN_INDEX_PAGE = "/login/index.html";
    public static final String LOGIN_FAILED_PAGE = "/login/login_failed.html";
    public static final String STATIC_DIR_PATH = "/static";
    public static final String REGISTRATION_FAILED_PAGE = "/registration/register_failed.html";
    public static final String ARTICLE_DIRECTORY = "/article.*";
    public static final String COMMENT_DIRECTORY = "/comment.*";
    public static final String USER_DIRECTORY = "/user/.*";
    public static final String IMAGE_DIRECTORY_FULL_PATH = "./src/main/resources/static/img/";
    public static final String USER_LIST_PAGE = "/user/list.html";
    public enum AccessLevel {
        PRIVATE(List.of(MAIN_DIRECTORY, ARTICLE_DIRECTORY, COMMENT_DIRECTORY, USER_DIRECTORY));

        private final List<String> urls;

        AccessLevel(List<String> urls) {
            this.urls = urls;
        }
        // 입력된 url이 인가가 필요한 url인지 정규표현식을 이용하여 검증

        public static boolean belongsToPublic(String url){
            return PRIVATE.urls.stream().noneMatch(u -> Pattern.compile(u).matcher(url).matches());
        }
    }
}
