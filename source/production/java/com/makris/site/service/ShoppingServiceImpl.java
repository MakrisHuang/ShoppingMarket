package com.makris.site.service;

import com.makris.site.entities.ShoppingItem;
import com.makris.site.repositories.ShoppingItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Inject
    ShoppingItemRepository shoppingItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingItem getShoppingItem(long shoppingItemId) {
        ShoppingItem item = this.shoppingItemRepository.findOne(shoppingItemId);
        return item;
    }

    @Override
    @Transactional
    public Page<ShoppingItem> getShoppingItems(String category, Pageable pageable) {
        return this.shoppingItemRepository.getByCategory(category, pageable);
    }
}
