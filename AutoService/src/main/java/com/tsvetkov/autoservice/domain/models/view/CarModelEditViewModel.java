package com.tsvetkov.autoservice.domain.models.view;

import java.util.List;

public class CarModelEditViewModel {
    private String model;
    private String horsePower;
    private List<String> brand;

    public CarModelEditViewModel() {
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

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
        this.brand = brand;
    }
}
