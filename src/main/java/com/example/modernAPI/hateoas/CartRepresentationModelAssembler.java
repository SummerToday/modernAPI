package com.example.modernAPI.hateoas;


import com.example.modernAPI.controllers.CartsController;
import com.example.modernAPI.entity.CartEntity;
import com.example.modernAPI.service.ItemService;
import com.packt.modern.api.model.Cart;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;


/**
 * @author : github.com/sharmasourabh
 * @project : Chapter04 - Modern API Development with Spring and Spring Boot Ed 2
 **/
@Component
public class CartRepresentationModelAssembler extends
        RepresentationModelAssemblerSupport<CartEntity, Cart> {

  private final ItemService itemService;

  /**
   * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and
   * resource type.
   */
  public CartRepresentationModelAssembler(ItemService itemService) {
    super(CartsController.class, Cart.class);
    this.itemService = itemService;
  }

  /**
   * Coverts the Card entity to resource
   *
   * @param entity
   */
  @Override
  public Cart toModel(CartEntity entity) {
    String uid = Objects.nonNull(entity.getUser()) ? entity.getUser().getId().toString() : null;
    String cid = Objects.nonNull(entity.getId()) ? entity.getId().toString() : null;
    Cart resource = new Cart();
    BeanUtils.copyProperties(entity, resource);
    resource.id(cid).customerId(uid).items(itemService.toModelList(entity.getItems()));
    resource.add(linkTo(methodOn(CartsController.class).getCartByCustomerId(uid)).withSelfRel());
    resource.add(linkTo(methodOn(CartsController.class).getCartItemsByCustomerId(uid))
        .withRel("cart-items"));
    return resource;
  }

  /**
   * Coverts the collection of Product entities to list of resources.
   *
   * @param entities
   */
  public List<Cart> toListModel(Iterable<CartEntity> entities) {
    if (Objects.isNull(entities)) {
      return List.of();
    }
    return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
        .collect(toList());
  }

}
