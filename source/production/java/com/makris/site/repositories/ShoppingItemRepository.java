package com.makris.site.repositories;

import com.makris.site.entities.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingItemRepository extends CrudRepository<ShoppingItem, Long> {
    Page<ShoppingItem> getByCategory(String category, Pageable pageable);
}

