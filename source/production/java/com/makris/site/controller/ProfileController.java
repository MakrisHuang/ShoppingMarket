package com.makris.site.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ProfileController {
    private static final String JSP_PROFILE = "profile/profile";

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(){
        return JSP_PROFILE;
    }
}
