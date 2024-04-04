package webserver;

public class Article {
    private final Long articleNumber;
    private final String writer;
    private final String title;
    private String contentString;
    private ImageFile imageFile;

    public Article(Long articleNumber, String writer, String title, String content, ImageFile imageFile){
        this.articleNumber = articleNumber;
        this.writer = writer;
        this.title = title;
        this.contentString = content;
        this.imageFile = imageFile;
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