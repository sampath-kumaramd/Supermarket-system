package com.example.order.Dto;

import com.example.order.Model.CartLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private CartLineItemsDto cartLineItemsDtoList;
}
