package com.example.lanchonetesystem.domain;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "items_order")
public class ItemsOrder extends RepresentationModel<ItemsOrder> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_order_id")
    private Long fullOrderId;
    @Column(name = "product_id")
    private Long productId;
    private Integer quantity;
    @Column(name = "unity_price")
    private Double unityPrice;

    public ItemsOrder(){}

    public ItemsOrder (Long productId, Integer quantity){
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFullOrderId() {
        return fullOrderId;
    }

    public void setFullOrderId(Long fullOrderId) {
        this.fullOrderId = fullOrderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnityPrice() {
        return unityPrice;
    }

    public void setUnityPrice(Double unityPrice) {
        this.unityPrice = unityPrice;
    }
}
