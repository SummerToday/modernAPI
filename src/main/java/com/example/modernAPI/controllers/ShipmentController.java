package com.example.modernAPI.controllers;


import com.example.modernAPI.hateoas.ShipmentRepresentationModelAssembler;
import com.example.modernAPI.service.ShipmentService;
import com.packt.modern.api.model.Shipment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
@RestController
public class ShipmentController implements ShipmentApi {

  private final ShipmentService service;
  private final ShipmentRepresentationModelAssembler assembler;

  public ShipmentController(ShipmentService service, ShipmentRepresentationModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<List<Shipment>> getShipmentByOrderId(@NotNull @Valid String id) {
    return ResponseEntity.ok(assembler.toListModel(service.getShipmentByOrderId(id)));
  }
}
