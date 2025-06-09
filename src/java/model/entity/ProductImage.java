package model.entity;

public class ProductImage {
    private int imageId;
    private int productId;
    private String imageUrl;
    private int isMain;
    private int displayOrder;
    private Integer isDeleted;

    // Constructor rỗng
    public ProductImage() {
    }

    // Constructor đầy đủ
    public ProductImage(int imageId, int productId, String imageUrl, int isMain, 
                      int displayOrder, Integer isDeleted) {
        this.imageId = imageId;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.displayOrder = displayOrder;
        this.isDeleted = isDeleted;
    }

    // Constructor không có imageId (dùng cho insert)
    public ProductImage(int productId, String imageUrl, int isMain, 
                       int displayOrder, int isDeleted) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.displayOrder = displayOrder;
        this.isDeleted = isDeleted;
    }

    // Getters và Setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setMain(int main) {
        isMain = main;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(Integer deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "imageId=" + imageId +
                ", productId=" + productId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isMain=" + isMain +
                ", displayOrder=" + displayOrder +
                ", isDeleted=" + isDeleted +
                '}';
    }
}