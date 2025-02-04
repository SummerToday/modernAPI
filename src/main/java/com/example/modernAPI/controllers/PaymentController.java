package com.example.modernAPI.controllers;

import com.example.modernAPI.hateoas.PaymentRepresentationModelAssembler;
import com.example.modernAPI.service.PaymentService;
import com.packt.modern.api.PaymentApi;

import com.packt.modern.api.model.Authorization;
import com.packt.modern.api.model.PaymentReq;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
@RestController
public class PaymentController implements PaymentApi {

  private final PaymentService service;
  private final PaymentRepresentationModelAssembler assembler;

  public PaymentController(PaymentService service, PaymentRepresentationModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  @Override
  public ResponseEntity<Authorization> authorize(@Valid PaymentReq paymentReq) {
    return null;
  }

  @Override
  public ResponseEntity<Authorization> getOrdersPaymentAuthorization(
      @NotNull @Valid String id) {
    return null;
  }
}
