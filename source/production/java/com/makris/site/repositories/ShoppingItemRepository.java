package com.makris.site.repositories;

import com.makris.site.entities.ShoppingItem;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingItemRepository extends CrudRepository<ShoppingItem, Long>{
//    Page<ShoppingItem> findByOrderId(long orderId, Pageable p);
}
