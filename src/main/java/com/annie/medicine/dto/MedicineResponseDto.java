package com.annie.medicine.dto;

import lombok.Data;

@Data
public class MedicineResponseDto {
    private Integer id;

    private String name;

    private String description;

    private String manufacturer;

    private String administration;

    private Integer quantity;

    private Double price;
}
