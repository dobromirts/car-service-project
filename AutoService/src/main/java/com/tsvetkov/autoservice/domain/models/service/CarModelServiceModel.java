package com.tsvetkov.autoservice.domain.models.service;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.Part;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarModelServiceModel {
    private String id;
    private String model;

    private String horsePower;
    private Car car;
    private String imageUrl;


    public CarModelServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
