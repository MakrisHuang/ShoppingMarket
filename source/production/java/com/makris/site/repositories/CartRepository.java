package com.makris.site.repositories;

import com.makris.site.entities.Cart;
import com.makris.site.entities.UserPrincipal;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long>{
    Cart findByCustomer(UserPrincipal customer);
}
