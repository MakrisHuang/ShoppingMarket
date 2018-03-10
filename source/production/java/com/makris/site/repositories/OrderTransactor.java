package com.makris.site.repositories;

import com.makris.site.entities.Order;
import com.makris.site.entities.UserPrincipal;

public interface OrderTransactor {
    Order getLatestOrderOfCustomer(UserPrincipal customer);
}
