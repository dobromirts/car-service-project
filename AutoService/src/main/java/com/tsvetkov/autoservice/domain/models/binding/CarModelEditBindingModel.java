package com.tsvetkov.autoservice.domain.models.binding;

import java.util.List;

public class CarModelEditBindingModel {
    private String model;
    private String horsePower;
    private String brand;

    public CarModelEditBindingModel() {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
