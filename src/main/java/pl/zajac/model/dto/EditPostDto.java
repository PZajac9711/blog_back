package pl.zajac.model.dto;

public class EditPostDto extends PostDto {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EditPostDto() {
    }

    public EditPostDto(String url, String title, String body, String id) {
        super(url, title, body);
        this.id = id;
    }
}
