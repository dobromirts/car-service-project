package com.tsvetkov.autoservice.domain.models.view;

import com.tsvetkov.autoservice.domain.models.service.CarModelServiceModel;
import com.tsvetkov.autoservice.domain.models.service.CategoryServiceModel;

import java.math.BigDecimal;
import java.util.List;

public class PartAllViewModel {
    private String id;
    private String name;
    private BigDecimal price;
    private BigDecimal workPrice;
    private List<CarModelViewModel> carModels;
    private List<CategoryServiceModel> categories;
    private String imageUrl;

    public PartAllViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getWorkPrice() {
        return workPrice;
    }

    public void setWorkPrice(BigDecimal workPrice) {
        this.workPrice = workPrice;
    }

    public List<CarModelViewModel> getCarModels() {
        return carModels;
    }

    public void setCarModels(List<CarModelViewModel> carModels) {
        this.carModels = carModels;
    }

    public List<CategoryServiceModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryServiceModel> categories) {
        this.categories = categories;
    }
}
