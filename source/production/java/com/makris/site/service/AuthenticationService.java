package com.makris.site.service;

import com.makris.site.entities.Customer;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthenticationService {
    // 驗證使用者是否為合法用戶
    Customer authenticateLogin(String username, String password);

    // 驗證在資料庫中有無該使用者存在，若無，回傳null
    boolean isEligibleForRegister(String email);

    // 寫入新使用者
    void saveNewUser(Customer customer, String password);

    // 更新使用者資料
    boolean updateUser(Customer customer, String newPassword);
}
