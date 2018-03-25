package com.makris.site.service.impl;

import com.makris.site.entities.ShoppingItem;
import com.makris.site.repositories.ShoppingItemRepository;
import com.makris.site.service.ShoppingService;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "shoppingItemCache", key = "#shoppingItemId")
    public ShoppingItem getShoppingItem(long shoppingItemId) {
        ShoppingItem item =  this.shoppingItemRepository.findOne(shoppingItemId);
        return item;
    }

    @Override
    @Transactional
    @Cacheable(value = "shoppingItemGroupCache", key = "#category")
    public Page<ShoppingItem> getShoppingItems(String category, Pageable pageable) {
        return this.shoppingItemRepository.getByCategory(category, pageable);
    }
}
