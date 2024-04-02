package webserver;

import static util.constants.Delimiter.EMPTY;

public class Article {
    private final Long articleNumber;
    private final String writer;
    private final String title;
    private byte[] contentByte;
    private String contentString;

    public Article(Long articleNumber, String writer, String title, byte[] content){
        this.articleNumber = articleNumber;
        this.writer = writer;
        this.title = title;
        this.contentByte = content;
        this.contentString = EMPTY;
    }

    public Article(Long articleNumber, String writer, String title, String content){
        this.articleNumber = articleNumber;
        this.writer = writer;
        this.title = title;
        this.contentString = content;
        this.contentByte = new byte[0];
    }

    public Long getArticleNumber() {
        return articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getContentByte() {
        return contentByte;
    }
    public String getContentString(){
        return contentString;
    }

    public String getWriter() {
        return writer;
    }
}