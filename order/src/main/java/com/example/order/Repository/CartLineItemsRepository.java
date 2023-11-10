package com.example.order.Repository;

import com.example.order.Model.Cart;
import com.example.order.Model.CartLineItems;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartLineItemsRepository extends JpaRepository<CartLineItems,Long> {

    @Transactional
    void deleteByCart(Cart cart);

    List<CartLineItems> findByCart(Cart cart);
}
