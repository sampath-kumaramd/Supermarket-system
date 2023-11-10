package com.example.delivery.Entity;
import com.example.delivery.type.deliveryStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long delivery_status_id;
    private deliveryStatusType delivery_status;
    private Date delivery_status_date;
}
