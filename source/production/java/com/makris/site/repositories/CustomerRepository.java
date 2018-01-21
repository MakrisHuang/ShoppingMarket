package com.makris.site.repositories;

import com.makris.site.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
    Customer getByUsername(String username);
    Customer getByEmail(String email);
}
