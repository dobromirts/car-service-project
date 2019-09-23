package com.tsvetkov.autoservice.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity{
    private String brand;



    public Car() {
    }

    @Column(name = "brand",nullable = false,unique = true)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



}
