package ru.stroy1click.web.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    private String title;

    private String description;

    private Double price;

    private Boolean inStock;

    private Integer categoryId;

    private Integer subcategoryId;

    private Integer productTypeId;
}
