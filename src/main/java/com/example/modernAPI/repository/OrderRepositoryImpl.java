package com.example.modernAPI.repository;


import com.example.modernAPI.entity.CartEntity;
import com.example.modernAPI.entity.ItemEntity;
import com.example.modernAPI.entity.OrderEntity;
import com.example.modernAPI.entity.OrderItemEntity;
import com.packt.modern.api.model.NewOrder;
import com.packt.modern.api.model.Order.StatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;


@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepositoryExt {

  @PersistenceContext private final EntityManager em;

  private final ItemRepository itemRepo;
  private final CartRepository cRepo;
  private final OrderItemRepository oiRepo;

  public OrderRepositoryImpl(
      EntityManager em, ItemRepository itemRepo, CartRepository cRepo, OrderItemRepository oiRepo) {
    this.em = em;
    this.itemRepo = itemRepo;
    this.cRepo = cRepo;
    this.oiRepo = oiRepo;
  }

  @Override
  public Optional<OrderEntity> insert(NewOrder m) {
    // Items are already in cart and saved in db when user places the order
    // Here, you can also populate the other Order details like address, card etc.

    // 장바구니 상품 조회
    Iterable<ItemEntity> dbItems = itemRepo.findByCustomerId(m.getCustomerId());
    List<ItemEntity> items = StreamSupport.stream(dbItems.spliterator(), false).toList();

    // 장바구니가 비어 있을 시 예외 발생
    if (items.size() < 1) {
      throw new ResourceNotFoundException(
          String.format("There is no item found in customer's (ID: %s) cart.", m.getCustomerId()));
    }

    // 총 주문 금액 계산
    BigDecimal total = BigDecimal.ZERO; // 현업에서는 float, double 보다 BigDecimal을 주로 사용. bc. 정확성
    for (ItemEntity i : items) {
      total = (BigDecimal.valueOf(i.getQuantity()).multiply(i.getPrice())).add(total);  // BigDecimal은 add(), subtract(), multiply(), divide(), setScale()의 다양한 연산을 지원한다.
    }

    // 주문을 데이터베이스에 삽입
    Timestamp orderDate = Timestamp.from(Instant.now());
    em.createNativeQuery(  // 네이티브 쿼리를 사용하여 속도 향상
            """
        INSERT INTO ecomm.orders (address_id, card_id, customer_id, order_date, total, status)
         VALUES(?, ?, ?, ?, ?, ?)
        """)
        .setParameter(1, m.getAddress().getId())
        .setParameter(2, m.getCard().getId())
        .setParameter(3, m.getCustomerId())
        .setParameter(4, orderDate)
        .setParameter(5, total)
        .setParameter(6, StatusEnum.CREATED.getValue())
        .executeUpdate();

    // 장바구니 엔티티 조회 및 예외 처리
    Optional<CartEntity> oCart = cRepo.findByCustomerId(UUID.fromString(m.getCustomerId()));
    CartEntity cart =
        oCart.orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format(
                        "Cart not found for given customer (ID: %s)", m.getCustomerId())));

    //장바구니에서 주문한 아이템 삭제
    itemRepo.deleteCartItemJoinById(
        cart.getItems().stream().map(ItemEntity::getId).collect(toList()), cart.getId());
    OrderEntity entity =
        (OrderEntity)
            em.createNativeQuery(
                    """
        SELECT o.* FROM ecomm.orders o WHERE o.customer_id = ? AND o.order_date >= ?
        """,
                    OrderEntity.class)
                .setParameter(1, m.getCustomerId())
                .setParameter(
                    2,
                    OffsetDateTime.ofInstant(orderDate.toInstant(), ZoneId.of("Z"))
                        .truncatedTo(ChronoUnit.MICROS)) // DB에서는 마이크로초까지만 저장을 하기 때문에 마이크로초까지만 남기고 이하 단위는 잘라버림.
                .getSingleResult();

    // 주문 아이템(order_items) 저장
    oiRepo.saveAll(
        cart.getItems().stream()
            .map(i -> new OrderItemEntity().setOrderId(entity.getId()).setItemId(i.getId()))
            .collect(toList()));
    return Optional.of(entity);
  }
}
