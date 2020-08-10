package com.example.govimart;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class SalePost {

    private String pCategory;
    private String pTitle;
    private double pQuantity;
    private String pGrade;
    private String pPrice;
    private String pAvailableDateFrom;
    private String pAvailableDateTo;
    private String pHarvestedDate;
    private String pLocation;
    private String pDistrict;
    private String pDescription;
    private String pImageUrl;
    private String pOwnerId;
    private String pOwnerName;
    private String pOwnerMobileNo;
    private Date pDateAdded;
    private String mKey;

    public SalePost(){
        // Empty constructor needed
    }

    public SalePost(String pCategory, String pTitle, double pQuantity, String pGrade, String pPrice, String pAvailableDateFrom, String pAvailableDateTo, String pHarvestedDate, String pLocation, String pDistrict, String pDescription, String pImageUrl, String pOwnerId, String pOwnerName, String pOwnerMobileNo, Date pDateAdded) {
        this.pCategory = pCategory;
        this.pTitle = pTitle;
        this.pQuantity = pQuantity;
        this.pGrade = pGrade;
        this.pPrice = pPrice;
        this.pAvailableDateFrom = pAvailableDateFrom;
        this.pAvailableDateTo = pAvailableDateTo;
        this.pHarvestedDate = pHarvestedDate;
        this.pLocation = pLocation;
        this.pDistrict = pDistrict;
        this.pDescription = pDescription;
        this.pImageUrl = pImageUrl;
        this.pOwnerId = pOwnerId;
        this.pOwnerName = pOwnerName;
        this.pOwnerMobileNo = pOwnerMobileNo;
        this.pDateAdded = pDateAdded;
    }

    // Getters
    public String getPCategory() {
        return pCategory;
    }

    public String getPTitle() {
        return pTitle;
    }

    public double getPQuantity() {
        return pQuantity;
    }

    public String getPGrade() {
        return pGrade;
    }

    public String getPPrice() {
        return pPrice;
    }

    public String getPAvailableDateFrom() {
        return pAvailableDateFrom;
    }

    public String getPAvailableDateTo() {
        return pAvailableDateTo;
    }

    public String getPHarvestedDate() {
        return pHarvestedDate;
    }

    public String getPLocation() {
        return pLocation;
    }

    public String getPDistrict() {
        return pDistrict;
    }

    public String getPDescription() {
        return pDescription;
    }

    public String getPImageUrl() {
        return pImageUrl;
    }

    public String getPOwnerId() {
        return pOwnerId;
    }

    public String getPOwnerName() {
        return pOwnerName;
    }

    public String getPOwnerMobileNo() {
        return pOwnerMobileNo;
    }

    public Date getPDateAdded() {
        return pDateAdded;
    }

    ////// Setters

    public void setPCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public void setPTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public void setPQuantity(double pQuantity) {
        this.pQuantity = pQuantity;
    }

    public void setPGrade(String pGrade) {
        this.pGrade = pGrade;
    }

    public void setPPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public void setPAvailableDateFrom(String pAvailableDateFrom) {
        this.pAvailableDateFrom = pAvailableDateFrom;
    }

    public void setPAvailableDateTo(String pAvailableDateTo) {
        this.pAvailableDateTo = pAvailableDateTo;
    }

    public void setPHarvestedDate(String pHarvestedDate) {
        this.pHarvestedDate = pHarvestedDate;
    }

    public void setPLocation(String pLocation) {
        this.pLocation = pLocation;
    }

    public void setPDistrict(String pDistrict) {
        this.pDistrict = pDistrict;
    }

    public void setPDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public void setPImageUrl(String pImageUrl) {
        this.pImageUrl = pImageUrl;
    }

    public void setPOwnerId(String pOwnerId) {
        this.pOwnerId = pOwnerId;
    }

    public void setPOwnerName(String pOwnerName) {
        this.pOwnerName = pOwnerName;
    }

    public void setPOwnerMobileNo(String pOwnerMobileNo) {
        this.pOwnerMobileNo = pOwnerMobileNo;
    }

    public void setPDateAdded(Date pDateAdded) {
        this.pDateAdded = pDateAdded;
    }

    /////////

    //
    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
    //
}
