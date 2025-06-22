package model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Category {
    private int categoryId;
    private Integer parentId;
    private String name;
    private String description;
    private String image;
    private String status;
    private int displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted; // Changed from boolean to Integer

    // Date format pattern
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public Category() {}

    // Constructor with essential fields
    public Category(int categoryId, String name, String status) {
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
    }

    // Constructor with all fields
    public Category(int categoryId, Integer parentId, String name, String description, String image, String status,
                    int displayOrder, String createdAt, String updatedAt, Integer isDeleted) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.name = name;
        this.description = description;
        this.image = image;
        this.status = status;
        this.displayOrder = displayOrder;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    // Getters and Setters for createdAt and updatedAt with String conversion
    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(DATE_FORMATTER) : null;
    }

    public void setCreatedAt(String createdAt) {
        if (createdAt != null && !createdAt.isEmpty()) {
            this.createdAt = LocalDateTime.parse(createdAt, DATE_FORMATTER);
        } else {
            this.createdAt = null;
        }
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(DATE_FORMATTER) : null;
    }

    public void setUpdatedAt(String updatedAt) {
        if (updatedAt != null && !updatedAt.isEmpty()) {
            this.updatedAt = LocalDateTime.parse(updatedAt, DATE_FORMATTER);
        } else {
            this.updatedAt = null;
        }
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Modified to use Integer instead of boolean
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Boolean convenience methods
    public boolean isDeleted() {
        return isDeleted != null && isDeleted == 1;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", displayOrder=" + displayOrder +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", isDeleted=" + isDeleted +
                '}';
    }
}