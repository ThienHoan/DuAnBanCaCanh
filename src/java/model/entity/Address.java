package model;

public class Address {
    private int addressId;
    private int userId;
    private String recipientName;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private boolean isDefault; // Maps to is_default in Addresses table
    private String addressType;
    private boolean isDeleted; // Maps to is_deleted

    public Address() {
    }

    public Address(int addressId, int userId, String recipientName, String phone, String province, String district, String ward, String addressDetail, boolean isDefault, String addressType, boolean isDeleted) {
        this.addressId = addressId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.phone = phone;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
        this.addressType = addressType;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public String getAddressDetail() { return addressDetail; }
    public void setAddressDetail(String addressDetail) { this.addressDetail = addressDetail; }
    public boolean getIsDefault() { return isDefault; } // Changed from isDefault() to getIsDefault()
    public void setIsDefault(boolean isDefault) { this.isDefault = isDefault; }
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
    public boolean getIsDeleted() { return isDeleted; } // Also update for consistency
    public void setDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }
}