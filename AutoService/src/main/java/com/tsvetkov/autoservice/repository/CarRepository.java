package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,String> {
}
