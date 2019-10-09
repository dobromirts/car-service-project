package com.tsvetkov.autoservice.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "car_models")
public class CarModel extends BaseEntityExtension{

    private String model;
    private String horsePower;
    private Car car;
    private String imageUrl;


    public CarModel() {
    }

    @Column(name = "model",nullable = false)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "horse_power",nullable = false)
    public String getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    @ManyToOne(targetEntity = Car.class)
    @JoinColumn(name = "car_id",referencedColumnName = "id")
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Column(name = "image_url",nullable = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
