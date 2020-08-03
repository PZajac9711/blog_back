package pl.zajac.model.dto;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "PostDto{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(url, postDto.url) &&
                Objects.equals(title, postDto.title) &&
                Objects.equals(body, postDto.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, body);
    }
}
