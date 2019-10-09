package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    Category findCategoriesByNameAndDeletedFalse(String name);

    List<Category> findAllByDeletedFalse();

    Optional<Category>findByIdAndDeletedFalse(String id);

}
