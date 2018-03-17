package com.makris.site.endpoint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makris.config.annotation.RestEndpoint;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;
import com.makris.site.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestEndpoint
@ResponseBody
public class AuthenticationRestEndPoint {
    private static final Logger logger = LogManager.getLogger();
    @Inject
    AuthenticationService authenticationService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<UserPrincipal> login(Map<String, Object> model, @RequestBody LoginForm form,
                        Errors errors){
        if (errors.hasErrors()){
            return null;
        }
        UserPrincipal customer = null;
        try{
            customer = this.authenticationService.authenticateLogin(form.getUsername(), form.getPassword());
        }catch (ConstraintViolationException e){
            model.put("validationErrors", e.getConstraintViolations());

        }

        if (customer != null){
            HttpHeaders headers = new HttpHeaders();
            headers.add("secret", JwtUtils.defaultSecret);
            return new ResponseEntity<UserPrincipal>(customer, headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<UserPrincipal>(customer, null, HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<UserPrincipal> register(@RequestBody RegisterForm form){
        boolean isEligible = false;
        try{
            isEligible = this.authenticationService.isEligibleForRegister(form.getEmail());
        }catch (ConstraintViolationException e){
            e.printStackTrace();
        }
        UserPrincipal customer = null;
        if (isEligible) {
            customer = new UserPrincipal();
            customer.setUsername(form.getUsername());
            customer.setAddress(form.getAddress());
            customer.setEmail(form.getEmail());
            customer.setTelPhone(form.getTelphone());
            customer.setPostCode(form.getPostCode());
            customer.setIsNonExpired("Y");
            customer.setIsAccountEnabled("Y");

            this.authenticationService.saveNewUser(customer, form.getPassword());

            HttpHeaders headers = new HttpHeaders();
            headers.set("secret", JwtUtils.defaultSecret);
            ResponseEntity<UserPrincipal> responseEntity =
                    new ResponseEntity<UserPrincipal>(customer, headers, HttpStatus.CREATED);
            return responseEntity;

        }else{
            return new ResponseEntity<UserPrincipal>(null, null, HttpStatus.NOT_MODIFIED);
        }
    }

    @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
            fieldVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class RegisterForm {

        private String username;

        private String password;

        private String email;

        private String telphone;

        private String address;

        private String postCode;

        @JsonProperty
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @JsonProperty
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @JsonProperty
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @JsonProperty
        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        @JsonProperty
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @JsonProperty
        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }

    @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
            fieldVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class LoginForm
    {

        private String username;

        private String password;

        @JsonProperty
        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        @JsonProperty
        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }
}
