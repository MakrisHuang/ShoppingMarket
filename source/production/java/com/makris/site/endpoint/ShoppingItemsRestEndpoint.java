package com.makris.site.endpoint;

import com.makris.Error;
import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ShoppingItemNotFoundException;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.service.ShoppingService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestEndpoint
public class ShoppingItemsRestEndpoint {
    @Inject
    ShoppingService shoppingService;

    @RequestMapping(value = "shopping", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public Page<ShoppingItem> getShoppingItemsWithCategory(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "category", required = true) String category,
        @RequestParam(value = "size", required = false) int size){
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        if (pageable == null){
            throw new ShoppingItemNotFoundException();
        }
        return this.shoppingService.getShoppingItems(category, pageable);
    }

    @Cacheable(value = "shoppingItemCache", key = "#result.id")
    @RequestMapping(value = "shopping/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public ShoppingItem getShoppingItemsWithId(@PathVariable(value = "id") int id){
        ShoppingItem item = this.shoppingService.getShoppingItem(id);
        if (item == null){
            throw new ShoppingItemNotFoundException(id);
        }
        return item;
    }

    @ExceptionHandler(ShoppingItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Error shoppingItemNotFound(ShoppingItemNotFoundException e){
        long id = e.getShoppingItemId();
        return new Error(4, "ShoppingItem [" + id + "] not found");
    }
}
