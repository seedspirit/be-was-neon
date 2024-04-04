package webserver;

import static util.constants.Delimiter.EMPTY;

public class Article {
    private final Long articleNumber;
    private final String writer;
    private final String title;
    private String contentString;
    private ImageFile imageFile;

    public Article(Long articleNumber, String writer, String title, ImageFile imageFile){
        this.articleNumber = articleNumber;
        this.writer = writer;
        this.title = title;
        this.imageFile = imageFile;
        this.contentString = EMPTY;
    }

    public Article(Long articleNumber, String writer, String title, String content){
        this.articleNumber = articleNumber;
        this.writer = writer;
        this.title = title;
        this.contentString = content;
        this.imageFile = null;
    }

    public Long getArticleNumber() {
        return articleNumber;
    }

    public String getTitle() {
        return title;
    }

    public ImageFile getImageFile() {
        return imageFile;
    }
    public String getContentString(){
        return contentString;
    }

    public String getWriter() {
        return writer;
    }
}