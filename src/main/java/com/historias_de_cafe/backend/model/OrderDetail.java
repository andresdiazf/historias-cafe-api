package com.historias_de_cafe.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private Long id;

    @Column(name = "quantity_products", nullable = false)
    private Integer quantityProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderDetail() {
    }

    public OrderDetail(Long id, Integer quantityProducts, Product product, Order order) {
        this.id = id;
        this.quantityProducts = quantityProducts;
        this.product = product;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityProducts() {
        return quantityProducts;
    }

    public void setQuantityProducts(Integer quantityProducts) {
        this.quantityProducts = quantityProducts;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
