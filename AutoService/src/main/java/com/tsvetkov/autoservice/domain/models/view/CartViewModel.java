package com.tsvetkov.autoservice.domain.models.view;

public class CartViewModel {
    private PartDetailsViewModel part;
    private Integer quantity;

    public CartViewModel() {
    }

    public PartDetailsViewModel getPart() {
        return part;
    }

    public void setPart(PartDetailsViewModel part) {
        this.part = part;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
