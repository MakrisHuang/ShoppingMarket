package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@WebController
public class HomeController {
    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView home(Map<String, Object> model, HttpSession session){
        logger.debug("get home");

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());
        model.put("registerForm", new RegisterForm());
        return new ModelAndView("home");
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
    }
}
