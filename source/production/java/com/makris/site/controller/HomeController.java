package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@WebController
public class HomeController {
    private static final Logger logger = LogManager.getLogger();

    // JSP page name
    private static final String JSP_HOME = "/shopping/items";

    private static final String ATTR_USERNAME       = "userName";

    @Inject
    JwtUtils jwtUtils;

    // unchecked
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView home(Map<String, Object> model, HttpServletRequest request){
        // 檢查、保持登入狀態
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);

        if (customer != null){
            model.put(ATTR_USERNAME, customer.getUsername());
        }
        return new ModelAndView(JSP_HOME);
    }
}
