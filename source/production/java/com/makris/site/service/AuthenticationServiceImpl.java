package com.makris.site.service;

import com.makris.site.entities.Customer;
import com.makris.site.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private static final Logger logger = LogManager.getLogger();
    private static final SecureRandom RANDOM;
    private static final int HASHING_ROUNDS = 10;
    @Inject
    CustomerRepository customerRepository;

    // 檢查是否支援java.security
    static{
        try {
            RANDOM = SecureRandom.getInstanceStrong();
        }catch (NoSuchAlgorithmException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    @Transactional
    public Customer authenticateLogin(String username, String password) {
        Customer customer = this.customerRepository.getByUsername(username);
        if (customer == null){
            logger.warn("Login failed for non-existent user {}", username);
            return null;
        }

        if (!BCrypt.checkpw(password, new String (customer.getPassword(), StandardCharsets.UTF_8))){
            // 驗證失敗
            logger.warn("Login failed for wrong password for user {}", username);
            return null;
        }

        logger.debug("User {} successfully authenticated", username);
        return customer;
    }

    @Override
    @Transactional
    public boolean isEligibleForRegiter(String email) {
        Customer customer = this.customerRepository.getByEmail(email);
        // 若用戶不存在，代表可以註冊
        return (customer == null) ? true : false;
    }

    @Override
    @Transactional
    public void saveNewUser(Customer customer, String password) {
        // 取得加密過的密碼
        String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
        customer.setPassword(BCrypt.hashpw(password, salt).getBytes());
        this.customerRepository.save(customer);
    }

    @Override
    @Transactional
    public boolean updateUser(Customer customer, String newPassword) {
        if (newPassword != null && newPassword.length() > 0){
            String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
            byte[] newPasswordByte = BCrypt.hashpw(newPassword, salt).getBytes();

            // 若密碼相同，不予更新
            return (customer.getPassword().equals(newPasswordByte)) ? false : true;
        }
        return false;
    }
}
