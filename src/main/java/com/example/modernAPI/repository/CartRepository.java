package com.example.modernAPI.repository;


import com.example.modernAPI.entity.CartEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
public interface CartRepository extends CrudRepository<CartEntity, UUID> {
  @Query("select c from CartEntity c join c.user u where u.id = :customerId")
  Optional<CartEntity> findByCustomerId(@Param("customerId") UUID customerId);
}
