package com.makris.config;

import com.makris.site.filter.AuthenticationFilter;
import com.makris.site.filter.LoggingFilter;
import com.makris.site.session.SessionListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

@SuppressWarnings("unused")
public class Bootstrap implements WebApplicationInitializer
{
    @Override
    public void onStartup(ServletContext container) throws ServletException
    {
        container.getServletRegistration("default").addMapping("/resource/*");

        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));
        container.addListener(SessionListener.class);

        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebServletContextConfiguration.class);
        ServletRegistration.Dynamic dispatcher = container.addServlet(
                "springWebDispatcher", new DispatcherServlet(webContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.setMultipartConfig(new MultipartConfigElement(
                null, 20_971_520L, 41_943_040L, 512_000
        ));
        dispatcher.addMapping("/");

        AnnotationConfigWebApplicationContext restContext =
                new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        DispatcherServlet restServlet = new DispatcherServlet(restContext);
        restServlet.setDispatchOptionsRequest(true);
        dispatcher = container.addServlet("springRestDispatcher", restServlet);
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/services/Rest/*");

        FilterRegistration.Dynamic registration = container.addFilter(
                "loggingFilter", new LoggingFilter()
        );
        registration.addMappingForUrlPatterns(null, false, "/*");

        registration = container.addFilter(
                "authenticationFilter", new AuthenticationFilter()
        );
        registration.addMappingForUrlPatterns(
                null, false, "/", "/home/", "/home/*", "/login/", "/login/*",
                "/register/", "/register/*", "/order/", "/order/*", "/shopping/", "/shopping/*"
        );
    }
}
