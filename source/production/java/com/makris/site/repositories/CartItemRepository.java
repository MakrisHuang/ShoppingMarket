package com.makris.site.repositories;

import com.makris.site.entities.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
