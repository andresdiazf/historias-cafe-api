package com.historias_de_cafe.backend.DTO;

import com.historias_de_cafe.backend.model.Roast;

public class ProductResponseDTO {

    private Long idProduct;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private String image;
    private String origin;
    private Roast roast;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Long idProduct, String name, String description, Double price, Integer stock, Long categoryId, String categoryName, String image, String origin, Roast roast) {
        this.idProduct = idProduct;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.image = image;
        this.origin = origin;
        this.roast = roast;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImagen(String image) {
        this.image = image;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Roast getRoast() {
        return roast;
    }

    public void setRoast(Roast roast) {
        this.roast = roast;
    }
}
