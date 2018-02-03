package com.makris.site.service;

import com.makris.site.entities.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShoppingService {
    // return a shopping item
    ShoppingItem getShoppingItem(long shoppingItemId);
    Page<ShoppingItem> getShoppingItems(String category, Pageable pageable);
}
