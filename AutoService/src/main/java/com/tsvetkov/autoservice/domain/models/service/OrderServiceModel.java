package com.tsvetkov.autoservice.domain.models.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceModel {
    private String id;
    private UserServiceModel user;
    private BigDecimal totalPrice;
    private BigDecimal totalWorkPrice;
    private BigDecimal totalPartsPrice;
    private String personName;
    private String phoneNumber;
    private String description;
    private LocalDateTime date;
    private List<PartServiceModel> parts;
    private LocalDateTime madeOn;

    private Boolean isConfirmed;


    private Boolean isFinished;


    public OrderServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PartServiceModel> getParts() {
        return parts;
    }

    public void setParts(List<PartServiceModel> parts) {
        this.parts = parts;
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


    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
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

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
