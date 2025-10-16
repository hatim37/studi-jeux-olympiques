package com.ecom.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Long price;
    private String description;
    private byte[] byteImg;
    @JsonIgnore
    private MultipartFile img;
    private Long quantity;

    public ProductDto(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

}
