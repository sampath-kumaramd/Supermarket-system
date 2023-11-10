package com.example.order.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String skuCode;
    private int quantity;
    private BigDecimal price;
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
