package com.tsvetkov.autoservice.repository;

import com.tsvetkov.autoservice.domain.entities.Order;
import com.tsvetkov.autoservice.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findAllByConfirmedTrueAndFinishedFalseOrderByDate();

    List<Order> findAllByConfirmedFalse();

    List<Order> findAllByFinishedTrue();

    List<Order> findAllByUserAndConfirmedTrueAndFinishedFalse(User user);
}
