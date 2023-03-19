package com.zaad.zaad.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String pincode;
    private AccountDetails accountDetails;

    private String referralCode;

    private String referredByCode;

    private int amount;

    private int availableCoupons;

    private String gender;

    private Date dob;

    private String level;

    private Date joinedDate;

    private Date expiryDate;

    private boolean paymentCompleted;

    private String district;

    private String language;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferredByCode() {
        return referredByCode;
    }

    public void setReferredByCode(String referredByCode) {
        this.referredByCode = referredByCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAvailableCoupons() {
        return availableCoupons;
    }

    public void setAvailableCoupons(int availableCoupons) {
        this.availableCoupons = availableCoupons;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", pincode='" + pincode + '\'' +
                ", accountDetails=" + accountDetails +
                ", referralCode='" + referralCode + '\'' +
                ", referredByCode='" + referredByCode + '\'' +
                ", amount=" + amount +
                ", availableCoupons=" + availableCoupons +
                ", gender='" + gender + '\'' +
                ", dob=" + dob +
                ", level='" + level + '\'' +
                ", joinedDate=" + joinedDate +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
