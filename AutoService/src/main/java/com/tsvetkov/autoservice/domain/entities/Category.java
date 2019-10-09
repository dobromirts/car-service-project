package com.tsvetkov.autoservice.domain.entities;

import javax.persistence.*;


@Entity
@Table(name = "categories")
public class Category extends BaseEntityExtension{

    private String name;
    private String imageUrl;


    public Category() {
    }

    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image_url",nullable = false)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
