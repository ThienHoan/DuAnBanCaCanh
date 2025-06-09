package model.entity.pAttribute;

public class ProductAttribute {
    private int attributeId;
    private String name;

    // Default constructor
    public ProductAttribute() {
    }

    // Constructor with all fields
    public ProductAttribute(int attributeId, String name) {
        this.attributeId = attributeId;
        this.name = name;
    }

    // Constructor without ID (for insertion)
    public ProductAttribute(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductAttribute{" +
                "attributeId=" + attributeId +
                ", name='" + name + '\'' +
                '}';
    }
}