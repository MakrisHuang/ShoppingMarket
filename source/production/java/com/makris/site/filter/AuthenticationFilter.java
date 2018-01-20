package com.makris.site.filter;

import com.makris.site.entities.UserPrincipal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

public class AuthenticationFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
        HttpSession session = ((HttpServletRequest)request).getSession(false);
        final Principal principal = UserPrincipal.getPrincipal(session);
        if(principal == null)
        {
//            ((HttpServletResponse)response).sendRedirect(
//                    ((HttpServletRequest) request).getContextPath() + "/"
//            );

            chain.doFilter(
                    new HttpServletRequestWrapper((HttpServletRequest)request)
                    {

                    },
                    response
            );
        }
        else
        {
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

    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void destroy() { }
}
