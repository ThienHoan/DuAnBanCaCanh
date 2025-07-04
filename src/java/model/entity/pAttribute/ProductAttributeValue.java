package model.entity.pAttribute;

public class ProductAttributeValue {
    private int valueId;
    private int productId;
    private int attributeId;
    private String value;
    private Integer isDeleted;


    // Default constructor
    public ProductAttributeValue() {
    }

    // Constructor with all fields (including new attributeName)
    public ProductAttributeValue(int valueId, int productId, int attributeId, String value, Integer isDeleted) {
        this.valueId = valueId;
        this.productId = productId;
        this.attributeId = attributeId;
        this.value = value;
        this.isDeleted = isDeleted;
    }

    // Constructor without ID (for insertion)
    public ProductAttributeValue(int productId, int attributeId, String value, Integer isDeleted) {
        this.productId = productId;
        this.attributeId = attributeId;
        this.value = value;
        this.isDeleted = isDeleted;
    }

    // Constructor without ID and isDeleted (for insertion with default isDeleted = 0)
    public ProductAttributeValue(int productId, int attributeId, String value) {
        this.productId = productId;
        this.attributeId = attributeId;
        this.value = value;
        this.isDeleted = 0;
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

    // New getter and setter for attributeName

    @Override
    public String toString() {
        return "ProductAttributeValue{" +
                "valueId=" + valueId +
                ", productId=" + productId +
                ", attributeId=" + attributeId +
                ", value='" + value + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}