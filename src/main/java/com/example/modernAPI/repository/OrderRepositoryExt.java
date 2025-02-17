package com.example.modernAPI.repository;


import com.example.modernAPI.entity.OrderEntity;
import com.packt.modern.api.model.NewOrder;

import java.util.Optional;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
public interface OrderRepositoryExt {
  Optional<OrderEntity> insert(NewOrder m);
}

