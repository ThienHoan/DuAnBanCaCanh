package model.entity;

import java.sql.Timestamp;

/**
 * Entity class for blog_posts table
 */
public class BlogPost {
    private int postId;
    private String title;
    private String content;
    private String summary;
    private String featuredImage;    private int authorId;
    private String authorName; // Join từ Users table
    private int categoryId; // Foreign key to blog_categories table
    private BlogCategory categoryObject; // Join từ blog_categories table
    private String tags;
    private String status; // draft, published, archived
    private int viewCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp publishedAt;
    private boolean isDeleted;

    // Constructors
    public BlogPost() {}    public BlogPost(String title, String content, String summary, String featuredImage, 
                   int authorId, int categoryId, String tags, String status) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.featuredImage = featuredImage;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.tags = tags;
        this.status = status;
        this.viewCount = 0;
        this.isDeleted = false;
    }

    // Getters and Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BlogCategory getCategoryObject() {
        return categoryObject;
    }

    public void setCategoryObject(BlogCategory categoryObject) {
        this.categoryObject = categoryObject;
        // Sync category ID
        if (categoryObject != null) {
            this.categoryId = categoryObject.getCategoryId();
        }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }    @Override
    public String toString() {
        return "BlogPost{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", authorName='" + authorName + '\'' +
                ", categoryId=" + categoryId +
                ", status='" + status + '\'' +
                ", viewCount=" + viewCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
