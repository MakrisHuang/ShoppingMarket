package com.makris.site.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class LoggingFilter implements Filter
{
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {

//        String id = UUID.randomUUID().toString();
//        ThreadContext.put("id", id);
//        Principal principal = UserPrincipal.getPrincipal(
//                ((HttpServletRequest) request).getSession(false)
//        );
//        if(principal != null) {
//            ThreadContext.put("username", principal.getName());
//        }
//        try
//        {
//            ((HttpServletResponse)response).setHeader("X-Makris-Request-Id", id);
//            chain.doFilter(request, response);
//        }
//        finally
//        {
//            ThreadContext.clearAll();
//        }
        String id = UUID.randomUUID().toString();
        Instant time = Instant.now();
        StopWatch timer = new StopWatch();
        try{
            timer.start();
            ((HttpServletResponse)response).setHeader("X-Makris-Request-Id", id);
            chain.doFilter(request, response);
        }finally {
            timer.stop();
            HttpServletRequest in = (HttpServletRequest)request;
            HttpServletResponse out = (HttpServletResponse)response;
            String length = out.getHeader("Content-Length");
            if (length == null || length.length() == 0)
                length = "-";
            logger.info(in.getRemoteAddr() + " -- [" + time + "]" + " \"" + in.getMethod() + " "
                    + in.getRequestURI() + " " + in.getProtocol() + " \"" + out.getStatus() + " " + length +
                    " " + timer
            );
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void destroy()
    {

    }
}
