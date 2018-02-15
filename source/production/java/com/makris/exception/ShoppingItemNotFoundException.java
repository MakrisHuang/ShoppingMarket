package com.makris.exception;

public class ShoppingItemNotFoundException extends RuntimeException {
    private long shoppingItemId = 0;
    public ShoppingItemNotFoundException() {

    }

    public ShoppingItemNotFoundException(long shoppingItemId) {
        this.shoppingItemId = shoppingItemId;
    }

    public long getShoppingItemId() {
        return shoppingItemId;
    }

    public void setShoppingItemId(long shoppingItemId) {
        this.shoppingItemId = shoppingItemId;
    }
}
