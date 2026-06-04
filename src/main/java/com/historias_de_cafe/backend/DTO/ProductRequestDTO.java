package com.historias_de_cafe.backend.DTO;

import com.historias_de_cafe.backend.model.Roast;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    private String image;

    @NotBlank(message = "El origen del producto no puede estar vacío")
    @Size(max = 100, message = "El origen es demasiado largo")
    private String origin;

    @NotNull(message = "El tipo de tostado es obligatorio")
    private Roast roast;

    public ProductRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
