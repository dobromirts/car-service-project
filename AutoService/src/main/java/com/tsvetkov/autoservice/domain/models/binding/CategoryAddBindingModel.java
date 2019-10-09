package com.tsvetkov.autoservice.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

public class CategoryAddBindingModel {
    private String name;
    private MultipartFile image;

    public CategoryAddBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
