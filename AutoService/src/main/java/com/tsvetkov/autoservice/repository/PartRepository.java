package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part,String> {
    Part findByName(String name);
}
