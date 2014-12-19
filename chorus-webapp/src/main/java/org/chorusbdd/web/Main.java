package org.chorusbdd.web;


import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Main {

    public static void main(final String[] arguments) throws Exception {
        final Server server = new Server(8085);
        final ContextHandler context = new ContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:application-context.xml");
        final ServletHandler handler = new ServletHandler();
        handler.addFilterWithMapping(org.springframework.web.filter.HttpPutFormContentFilter.class, "/*", 0);

        //handler.addFilter(org.springframework.web.filter.HttpPutFormContentFilter);
        handler.addServletWithMapping(new ServletHolder(dispatcherServlet), "/*");
        context.addHandler(handler);
        server.start();
    }
}