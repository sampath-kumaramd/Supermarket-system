package com.example.delivery.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long delivery_id;
    private Long order_id;
    private Long delivery_person_id;
    private Date delivery_date;

    @OneToOne(cascade = CascadeType.ALL)
    private DeliveryAddress delivery_address_id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DeliveryStatus> delivery_status;
}
