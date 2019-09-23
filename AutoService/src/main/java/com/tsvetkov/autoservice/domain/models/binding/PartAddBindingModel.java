package com.tsvetkov.autoservice.domain.models.binding;

import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;
import com.tsvetkov.autoservice.domain.models.view.CategoriesAllViewModel;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class PartAddBindingModel {
    private String name;
    private String description;
    private BigDecimal price;
    private MultipartFile image;
    private List<String> categories;
    private List<String> carModels;

    public PartAddBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCarModels() {
        return carModels;
    }

    public void setCarModels(List<String> carModels) {
        this.carModels = carModels;
    }
}
