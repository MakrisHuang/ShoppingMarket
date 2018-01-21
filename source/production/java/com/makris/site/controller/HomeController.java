package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.Customer;
import com.makris.site.entities.UserPrincipal;
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
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@WebController
public class HomeController {
    private static final Logger logger = LogManager.getLogger();

    // JSP page name
    private static final String JSP_HOME = "home";
    private static final String JSP_LOGIN ="login";

    @Inject
    AuthenticationService authenticationService;

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView home(Map<String, Object> model, HttpSession session){
        logger.debug("get home");

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        model.put("registerForm", new RegisterForm());
        return new ModelAndView(JSP_HOME);
    }

    @RequestMapping(value = "home", method = RequestMethod.POST)
    public ModelAndView login(Map<String, Object> model, HttpSession session,
                        HttpServletRequest request, @Valid LoginForm form,
                        Errors errors){
        if (UserPrincipal.getPrincipal(session) != null){
            // 重新導向到home頁面
            return this.getHomeRedirect();
        }
        if (errors.hasErrors()){
            form.setPassword(null);
            return null;
        }
        Customer customer;
        try{
            customer = this.authenticationService.
                    authenticateLogin(form.getUsername(), form.getPassword());
        }catch (ConstraintViolationException e){
            form.setPassword(null);
            model.put("validationErrors", e.getConstraintViolations());
            return new ModelAndView(JSP_HOME);
        }

        if (customer == null){
            form.setPassword(null);
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView(JSP_LOGIN);
        }

        UserPrincipal.setPrincipal(session, customer);
        request.changeSessionId();
        return new ModelAndView(JSP_HOME);
    }

    @RequestMapping(value = "home", method = RequestMethod.POST)
    public ModelAndView register(Map<String, Object> model, HttpSession session,
                              HttpServletRequest request, @Valid RegisterForm form,
                              Errors errors){
        if (UserPrincipal.getPrincipal(session) != null){
            // 重新導向到home頁面
            return getHomeRedirect();
        }
        boolean isEligible;
        try{
            isEligible = this.authenticationService.isEligibleForRegiter(form.getEmail());
        }catch (ConstraintViolationException e){
            form.setPassword(null);
            model.put("validationErrors", e.getConstraintViolations());
            return new ModelAndView(JSP_HOME);
        }

        if (isEligible){
            Customer customer = new Customer();
            customer.setUsername(form.getUsername());
            customer.setAddress(form.getAddress());
            customer.setTelPhone(form.getTelphone());
            customer.setPostCode(form.getPostCode());

            this.authenticationService.saveNewUser(customer, form.getPassword());
        }
        return new ModelAndView(JSP_HOME);
    }

    public ModelAndView getHomeRedirect(){
        return new ModelAndView(new RedirectView("/home", true, false));
    }

    public static class LoginForm
    {

        private String username;

        private String password;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }

    private static class RegisterForm {

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }
    }
}
