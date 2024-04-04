package webserver.httpMessage.httpRequest.body;

import webserver.ImageFile;

public interface FormBody extends RequestBody {
    @Override
    public byte[] toBytes();
    public String getArticleTitle();
    public String getArticleContent();
    public ImageFile getArticleImageFile();
}