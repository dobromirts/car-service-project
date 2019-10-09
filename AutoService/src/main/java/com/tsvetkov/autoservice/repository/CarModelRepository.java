package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Car;
import com.tsvetkov.autoservice.domain.entities.CarModel;
import com.tsvetkov.autoservice.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel,String> {

    List<CarModel> findAllCarsModelsByCarAndDeletedFalse(Car car);

    List<CarModel> findAllByDeletedFalse();

    CarModel findModelByModelAndDeletedFalse(String model);
}
