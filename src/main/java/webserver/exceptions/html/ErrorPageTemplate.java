package webserver.exceptions.html;

import webserver.httpMessage.HttpStatus;

import java.util.Arrays;

enum ErrorPageTemplate {
    NOT_FOUND("""
            <p class="page-title">404 Error - Page Not Found</p>
                    <p class="page-title">죄송합니다. 찾고 있는 페이지를 찾을 수 없습니다.</p>
                    <a class="btn btn_contained btn_size_m" href="/">홈으로 돌아가기</a>
            """),

    INTERNAL_ERROR("""
            <p class="page-title">죄송합니다. 내부 서버 오류로 인해 요청을 처리할 수 없습니다.</p>
                    <p class="page-title">문제가 지속되면, 웹사이트 관리자에게 연락해 주세요.</p>
                    <a class="btn btn_contained btn_size_m" href="/">홈으로 돌아가기</a>
            """),

    BAD_REQUEST("""
                    <p class="page-title">400 Bad Request - 잘못된 요청입니다</p>
                    <a class="btn btn_contained btn_size_m" href="/">홈으로 돌아가기</a>
            """),

    METHOD_NOT_ALLOWED("""
                    <p class="page-title">405 Error - Method not Allowed</p>
                    <p class="page-title">지원하지 않는 메서드로 요청하였습니다</p>
                    <a class="btn btn_contained btn_size_m" href="/">홈으로 돌아가기</a>
            """);

    private final String template;

    ErrorPageTemplate(String template){
        this.template = template;
    }

    public static String getHtmlOf(HttpStatus httpStatus){
        try{
            return ErrorPageTemplate.valueOf(httpStatus.name()).template;
        } catch (IllegalArgumentException e){
            return INTERNAL_ERROR.template;
        }
    }
}
