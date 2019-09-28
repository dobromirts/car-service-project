package com.tsvetkov.autoservice.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

public class CarBindingModel {

    private String brand;
    private MultipartFile image;

    public CarBindingModel() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
