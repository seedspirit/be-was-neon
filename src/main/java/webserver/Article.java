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

    public String generatePostHTML(){
        String WRITER_MARKER = "articleWriter";
        String CONTENT_MARKER = "articleContent";
        String IMAGE_MARKER = "articleImage";
        String html = HTMLFormat.replaceFirst(WRITER_MARKER, writer)
                .replaceFirst(CONTENT_MARKER, contentString)
                .replaceFirst(IMAGE_MARKER, generateImageTage());
        return html;
    }

    private String generateImageTage(){
        return "src=\"data:" + imageFile.getMimeType() + ";base64," + imageFile.toBase64() + "\"";
    }

    private final String HTMLFormat = """
            <div class="post">
            <div class="post__account">
                        <img class="post__account__img" />
                        <p class="post__account__nickname">articleWriter</p>
                      </div>
                      <img class="post__img" articleImage />
                      <div class="post__menu">
                        <ul class="post__menu__personal">
                          <li>
                            <button class="post__menu__btn">
                              <img src="../img/like.svg" />
                            </button>
                          </li>
                          <li>
                            <button class="post__menu__btn">
                              <img src="../img/sendLink.svg" />
                            </button>
                          </li>
                        </ul>
                        <button class="post__menu__btn">
                          <img src="../img/bookMark.svg" />
                        </button>
                      </div>
                      <p class="post__article">
                        articleContent
                      </p>
                      </div>
            """;
}