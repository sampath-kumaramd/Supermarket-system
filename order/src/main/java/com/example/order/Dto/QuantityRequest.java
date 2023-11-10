package com.example.order.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuantityRequest {
    private List<String> skuCodes;
    private List<Integer> Quantities;
}
