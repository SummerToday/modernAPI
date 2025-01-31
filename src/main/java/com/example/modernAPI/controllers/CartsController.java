package com.example.modernAPI.controllers;

import static org.springframework.http.ResponseEntity.ok;

import com.packt.modern.api.CartApi;
import com.packt.modern.api.model.Item;
import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartsController implements CartApi {

    private static final Logger log = LoggerFactory.getLogger(CartsController.class);

    @Override
    public ResponseEntity<List<Item>> addCartItemsByCustomerId(String customerId, @Valid Item item) {
        log.info("고객 ID 요청: {}\nItem: {}", customerId, item);
        return ok(Collections.EMPTY_LIST);
    }

    @Override
    public ResponseEntity<List<Item>> getCartByCustomerId(String customerId){
        throw new RuntimeException("수동 예외 발생 (Manual Exception thrown)");
    }

}