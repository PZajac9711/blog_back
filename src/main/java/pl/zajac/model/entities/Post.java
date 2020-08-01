package pl.zajac.model.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "posts")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "published")
    private boolean isPublished;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Post() {
    }

    private Post(String imageUrl, String title, String body, LocalDateTime publicationDate, String authorName, boolean isPublished) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.body = body;
        this.publicationDate = publicationDate;
        this.authorName = authorName;
        this.isPublished = isPublished;
    }

    public static class Builder {
        private String imageUrl;
        private String title;
        private String body;
        private LocalDateTime publicationDate;
        private String authorName;
        private boolean isPublished;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setPublicationDate(LocalDateTime publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder setAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder setPublished(boolean published) {
            isPublished = published;
            return this;
        }

        public Post build() {
            return new Post(imageUrl, title, body, publicationDate, authorName, isPublished);
        }
    }
}
