package com.makris.site.repositories;

import com.makris.site.entities.UserPrincipal;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<UserPrincipal, Long>{
    UserPrincipal getByUsername(String username);
    UserPrincipal getByEmail(String email);
}
