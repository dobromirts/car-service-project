package com.tsvetkov.autoservice.domain.models.view;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public class OrderViewModel {
    private ProfileViewModel user;
    private BigDecimal totalPrice;
    private BigDecimal totalWorkPrice;
    private BigDecimal totalPartsPrice;
    private String personName;
    private String phoneNumber;
    private LocalDateTime madeOn;
    private LocalDateTime date;

    public OrderViewModel() {
    }

    public ProfileViewModel getUser() {
        return user;
    }

    public void setUser(ProfileViewModel user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalWorkPrice() {
        return totalWorkPrice;
    }

    public void setTotalWorkPrice(BigDecimal totalWorkPrice) {
        this.totalWorkPrice = totalWorkPrice;
    }

    public BigDecimal getTotalPartsPrice() {
        return totalPartsPrice;
    }

    public void setTotalPartsPrice(BigDecimal totalPartsPrice) {
        this.totalPartsPrice = totalPartsPrice;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getMadeOn() {
        return madeOn;
    }

    public void setMadeOn(LocalDateTime madeOn) {
        this.madeOn = madeOn;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
