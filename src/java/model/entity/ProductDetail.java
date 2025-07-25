package model.entity;

public class ProductDetail {
    private int productDetailId;
    private int productId;
    private String scientificName;
    private String commonName;
    private String origin;
    private String size;
    private String lifespan;
    private String waterType;
    private String waterTemperature;
    private String waterPh;
    private String diet;
    private String breedingDifficulty;
    private String careLevel;
    private String compatibility;
    private Integer isDeleted; // Using Integer to match the pattern from Product entity

    // Default constructor
    public ProductDetail() {
    }

    // Constructor with all fields
    public ProductDetail(int productDetailId, int productId, String scientificName, 
                        String commonName, String origin, String size, String lifespan, 
                        String waterType, String waterTemperature, String waterPh, 
                        String diet, String breedingDifficulty, String careLevel, 
                        String compatibility, Integer isDeleted) {
        this.productDetailId = productDetailId;
        this.productId = productId;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.origin = origin;
        this.size = size;
        this.lifespan = lifespan;
        this.waterType = waterType;
        this.waterTemperature = waterTemperature;
        this.waterPh = waterPh;
        this.diet = diet;
        this.breedingDifficulty = breedingDifficulty;
        this.careLevel = careLevel;
        this.compatibility = compatibility;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLifespan() {
        return lifespan;
    }

    public void setLifespan(String lifespan) {
        this.lifespan = lifespan;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public String getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(String waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public String getWaterPh() {
        return waterPh;
    }

    public void setWaterPh(String waterPh) {
        this.waterPh = waterPh;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getBreedingDifficulty() {
        return breedingDifficulty;
    }

    public void setBreedingDifficulty(String breedingDifficulty) {
        this.breedingDifficulty = breedingDifficulty;
    }

    public String getCareLevel() {
        return careLevel;
    }

    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Boolean convenience methods (following the pattern from Product entity)
    public boolean isDeleted() {
        return isDeleted != null && isDeleted == 1;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted ? 1 : 0;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "productDetailId=" + productDetailId +
                ", productId=" + productId +
                ", scientificName='" + scientificName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", origin='" + origin + '\'' +
                ", size='" + size + '\'' +
                ", lifespan='" + lifespan + '\'' +
                ", waterType='" + waterType + '\'' +
                ", careLevel='" + careLevel + '\'' +
                '}';
    }
}