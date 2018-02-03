package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.service.ShoppingService;
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
        return this.shoppingService.getShoppingItems(category, pageable);
    }

    @RequestMapping(value = "shopping/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public ShoppingItem getShoppingItemsWithId(@PathVariable(value = "id") int id){
        ShoppingItem item = this.shoppingService.getShoppingItem(id);
        return item;
    }

}
