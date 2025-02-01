package com.example.modernAPI.repository;


import com.example.modernAPI.entity.CardEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
public interface CardRepository extends CrudRepository<CardEntity, UUID> {
}

