package webserver.exceptions.html;

import webserver.httpMessage.HttpStatus;

public class ExceptionHTMLGenerator {
    private static String TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <link href="../css/reset.css" rel="stylesheet" />
                <link href="../css/global.css" rel="stylesheet" />
            </head>
            <body>
            <div class="container">
                <header class="header">
                    <a href="/"><img src="../img/signiture.svg" /></a>
                    <ul class="header__menu">
                        <li class="header__menu__item">
                            <a class="btn btn_contained btn_size_s" href="/login">로그인</a>
                        </li>
                        <li class="header__menu__item">
                            <a class="btn btn_ghost btn_size_s" href="/registration">
                                회원 가입
                            </a>
                        </li>
                    </ul>
                </header>
                <div class="page">
                    %s
                </div>
            </div>
            </body>
            </html>
            """;
    public static String getHtml(HttpStatus errorHttpStatus){
       return String.format(TEMPLATE, ErrorPageTemplate.getHtmlOf(errorHttpStatus));
    }
}
