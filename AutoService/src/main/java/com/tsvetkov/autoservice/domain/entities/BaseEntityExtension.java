package com.tsvetkov.autoservice.domain.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntityExtension extends BaseEntity{
    private Boolean isDeleted;

    public BaseEntityExtension() {
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
