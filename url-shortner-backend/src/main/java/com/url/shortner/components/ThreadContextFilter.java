//package com.url.shortner.components;
//
//import jakarta.servlet.*;
//import org.slf4j.MDC;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE) // Ensures this filter runs first
//public class ThreadContextFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        try {
//            // Continue the filter chain
//            MDC.put("uuId", "uuid_" + UUID.randomUUID().toString().replace("-", ""));
//            chain.doFilter(request, response);
//        } finally {
//            // Cleanup MDC to prevent memory leaks even if exception occurs
//            MDC.clear();
//        }
//    }
//
//}