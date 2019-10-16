package com.tsvetkov.autoservice.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private User user;
    private BigDecimal totalPrice;
    private BigDecimal totalWorkPrice;
    private BigDecimal totalPartsPrice;
    private String personName;
    private String phoneNumber;
    private String description;
    private List<Part> parts;
    private LocalDateTime madeOn;
    private LocalDateTime date;
    private Boolean isConfirmed;

    private Boolean isFinished;

    public Order() {
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Column(name = "person_name", nullable = false)
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Column(name = "phone_number", nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(targetEntity = Part.class)
    @JoinTable(name = "orders_parts", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Column(name = "finished_on")
    public LocalDateTime getMadeOn() {
        return madeOn;
    }

    public void setMadeOn(LocalDateTime madeOn) {
        this.madeOn = madeOn;
    }

    @Column(name = "date", nullable = false)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Column(name = "is_confirmed", columnDefinition = "BOOLEAN")
    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Column(name = "total_work_price")
    public BigDecimal getTotalWorkPrice() {
        return totalWorkPrice;
    }

    public void setTotalWorkPrice(BigDecimal totalWorkPrice) {
        this.totalWorkPrice = totalWorkPrice;
    }

    @Column(name = "total_parts_price")
    public BigDecimal getTotalPartsPrice() {
        return totalPartsPrice;
    }

    public void setTotalPartsPrice(BigDecimal totalPartsPrice) {
        this.totalPartsPrice = totalPartsPrice;
    }

    @Column(name = "is_finished", columnDefinition = "BOOLEAN")
    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
