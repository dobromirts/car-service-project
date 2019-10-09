package com.tsvetkov.autoservice.domain.models.binding;


import org.springframework.web.multipart.MultipartFile;

public class CarModelAddBindingModel {
    private String model;
    private String horsePower;
    private String car;
    private MultipartFile image;

    public CarModelAddBindingModel() {
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

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
