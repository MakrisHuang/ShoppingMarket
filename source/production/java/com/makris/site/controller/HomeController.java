package com.makris.site.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makris.config.annotation.WebController;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;
import com.makris.site.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@WebController
public class HomeController {
    private static final Logger logger = LogManager.getLogger();

    // JSP page name
    private static final String JSP_HOME = "/shopping/items";

    private static final String ATTR_FORM_REGISTER  = "registerForm";
    private static final String ATTR_USERNAME       = "userName";

    @Inject
    AuthenticationService authenticationService;
    @Inject
    JwtUtils jwtUtils;

    // unchecked
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView home(Map<String, Object> model, HttpServletRequest request){
        // 檢查、保持登入狀態
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);

        if (customer != null){
            model.put("loginFailed", false);
            model.put(ATTR_USERNAME, customer.getUsername());
        }
        else {
            model.put("loginFailed", true);
        }
        model.put(ATTR_FORM_REGISTER, new RegisterForm());
        return new ModelAndView(JSP_HOME);
    }



    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logout(Map<String, Object> model, HttpServletRequest request){
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);
        if (customer != null){

            // logout successfully
            model.put("loginFailed", true);
        }else{
            // logout fail
            model.put("loginFailed", false);

        }
        model.put(ATTR_FORM_REGISTER, new RegisterForm());
        return getHomeRedirect();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ModelAndView register(Map<String, Object> model, HttpServletRequest request,
                                 @Valid RegisterForm form, Errors errors){
        if (jwtUtils.getUserFromHttpRequest(request, false) != null){
            // 重新導向到home頁面
            return getHomeRedirect();
        }
        if (errors.hasErrors()){
            form.setPassword(null);
            return getHomeRedirect();
        }
        boolean isEligible;
        try{
            isEligible = this.authenticationService.isEligibleForRegister(form.getEmail());
        }catch (ConstraintViolationException e){
            form.setPassword(null);
            model.put("validationErrors", e.getConstraintViolations());
            return new ModelAndView(JSP_HOME);
        }

        if (isEligible){
            UserPrincipal customer = new UserPrincipal();
            customer.setUsername(form.getUsername());
            customer.setAddress(form.getAddress());
            customer.setEmail(form.getEmail());
            customer.setTelPhone(form.getTelphone());
            customer.setPostCode(form.getPostCode());

            this.authenticationService.saveNewUser(customer, form.getPassword());

//            UserPrincipal.setPrincipal(session, customer);
//            request.changeSessionId();

            model.put("userName", customer.getName());
            model.put("loginForm", null);
            model.put("registerForm", null);
            model.put("loginFailed", false);
        }

        // register failed
        model.put("loginForm", new LoginForm());
        model.put("registerForm", form);
        model.put("loginFailed", true);

        return getHomeRedirect();
    }

    private ModelAndView getHomeRedirect(){
        RedirectView redirectView = new RedirectView("/", false, false);
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
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

    public static class RegisterForm {

        private String username;

        private String password;

        private String email;

        private String telphone;

        private String address;

        private String postCode;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }
}
