package model.entity;

public class ProductAttribute {
    private int valueId;
    private int productId;
    private int attributeId;
    private String attributeName;
    private String value;
    private Integer isDeleted;

    // Default constructor
    public ProductAttribute() {
    }

    // Constructor with all fields
    public ProductAttribute(int valueId, int productId, int attributeId, String attributeName, String value, Integer isDeleted) {
        this.valueId = valueId;
        this.productId = productId;
        this.attributeId = attributeId;
        this.attributeName = attributeName;
        this.value = value;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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
        return "ProductAttribute{" +
                "valueId=" + valueId +
                ", productId=" + productId +
                ", attributeId=" + attributeId +
                ", attributeName='" + attributeName + '\'' +
                ", value='" + value + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}