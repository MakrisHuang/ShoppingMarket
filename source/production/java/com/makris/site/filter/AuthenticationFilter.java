package com.makris.site.filter;

import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;

public class AuthenticationFilter implements Filter
{
    public static final String KEY_TOKEN_HEADER = "tokenHeader";
    @Inject
    JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
        String token = ((HttpServletRequest)request).getHeader(AuthenticationFilter.KEY_TOKEN_HEADER);
        if (token != null){

            final UserPrincipal principal = jwtUtils.getUserFromToken(token, false);
            if(principal == null)
            {
                chain.doFilter(request, response);
            }
            else
            {
                // 驗證並取得使用者身份成功，傳入到下一個Filter
                chain.doFilter(
                        new HttpServletRequestWrapper((HttpServletRequest)request)
                        {
                            @Override
                            public Principal getUserPrincipal() {
                                return principal;
                            }
                        },
                        response
                );
            }
        }
        else{
            // First user
            chain.doFilter(request, response);
        }
    }



    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void destroy() { }
}
