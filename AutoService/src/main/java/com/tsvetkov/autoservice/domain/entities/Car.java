//package com.tsvetkov.autoservice.domain.entities;
//
//import javax.persistence.Entity;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//import java.util.List;
//
//@Entity
//@Table(name = "cars")
//public class Car extends BaseEntity{
//    private String brand;
//    private String model;
//    private List<Part>parts;
//
//    public Car() {
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//    @ManyToMany(targetEntity = Category.class,mappedBy = "cars")
//    public List<Part> getParts() {
//        return parts;
//    }
//
//    public void setParts(List<Part> parts) {
//        this.parts = parts;
//    }
//}
