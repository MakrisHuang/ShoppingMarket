package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.site.controller.HomeController;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;
import com.makris.site.service.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestEndpoint
@ResponseBody
public class AuthenticationRestEndPoint {
    private static final Logger logger = LogManager.getLogger();
    @Inject
    AuthenticationService authenticationService;
    @Inject
    JwtUtils jwtUtils;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(Map<String, Object> model, HttpServletResponse response,
                        HttpServletRequest request, @RequestBody HomeController.LoginForm form,
                        Errors errors){
        logger.info("username" + form.getUsername());
        if (jwtUtils.getUserFromHttpRequest(request, false) != null){
            // 重新導向到home頁面
        }
        if (errors.hasErrors()){
            form.setPassword(null);
            return null;
        }
        UserPrincipal customer;
        try{
            customer = this.authenticationService.authenticateLogin(form.getUsername(), form.getPassword());
        }catch (ConstraintViolationException e){
            form.setPassword(null);
            model.put("validationErrors", e.getConstraintViolations());
            return "shopping/items";
        }

        if (customer == null){
            form.setPassword(null);
            model.put("loginFailed", true);

            return "shopping/items";
        }

        // send jwt key
        response.setHeader(JwtUtils.KEY_TOKEN_HEADER, JwtUtils.defaultSecret);

        model.put("loginFailed", false);
        return JwtUtils.defaultSecret;
    }
}
