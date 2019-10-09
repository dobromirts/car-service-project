package com.tsvetkov.autoservice.repository;


import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.entities.Category;
import com.tsvetkov.autoservice.domain.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part,String> {
    Part findByName(String name);

    List<Part> findAllPartsByCarModels(CarModel carModel);

    List<Part> findAllByCarModelsAndCategories(CarModel carModel, Category category);

}
