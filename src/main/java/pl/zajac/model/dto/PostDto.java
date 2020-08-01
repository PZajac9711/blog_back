package pl.zajac.model.dto;

public class PostDto {
    private String url;
    private String title;
    private String body;

    public PostDto() {
    }

    public PostDto(String url, String title, String body) {
        this.url = url;
        this.title = title;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
