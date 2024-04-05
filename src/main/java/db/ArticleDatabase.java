package db;

import webserver.Article;

import java.util.*;

public class ArticleDatabase {
    private static final Map<Long, Article> articleDatabase = new LinkedHashMap<>();

    public static void addRecord(Long seq, Article article){
        articleDatabase.put(seq, article);
    }

    public static Article getArticle(Long seq) {
        return articleDatabase.get(seq);
    }

    public static void removeRecord(Long seq){
        articleDatabase.remove(seq);
    }

    public static boolean isAnyArticleExists(){
        return !articleDatabase.isEmpty();
    }

    public static String generateArticleListHTML() {
        final String TABLE_HTML_FORMAT = """
                <tbody>
                    %s
                </tbody>
                            """;
        StringBuilder result = new StringBuilder();
        for (Article article : articleDatabase.values()){
            result.append("<tr>\n")
                    .append(String.format("<td>%s</td>\n", article.getTitle()))
                    .append(String.format("<td>%s</td>\n", article.getWriter()))
                    .append("</tr>\n");
        }
        return String.format(TABLE_HTML_FORMAT, result.toString());
    }

    public static String generateArticlePostHTML() {
        StringBuilder builder = new StringBuilder();
        for (Article article : articleDatabase.values()){
            builder.append(article.generatePostHTML());
        }
        return builder.toString();
    }
}