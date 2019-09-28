package com.tsvetkov.autoservice.domain.models.binding;

import org.springframework.web.multipart.MultipartFile;

public class CategoryBindingModel {
    private String name;
    private MultipartFile image;

    public CategoryBindingModel() {
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
