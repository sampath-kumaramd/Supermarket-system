package com.example.order.Dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressDto {
    private Long Delivery_address_id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

}
