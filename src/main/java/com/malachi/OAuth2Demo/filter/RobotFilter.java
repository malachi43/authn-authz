package com.malachi.OAuth2Demo.filter;

import com.malachi.OAuth2Demo.authentication.RobotAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class RobotFilter extends OncePerRequestFilter {
    private final static String HEADER_NAME = "x-robot-password";
    private final AuthenticationManager authenticationManager;

    public RobotFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Hello in robot filter....");
        boolean hasXRobotPasswordHeader = Collections.list(request.getHeaderNames()).contains(HEADER_NAME);
        if(!hasXRobotPasswordHeader){
            filterChain.doFilter(request,response);
            return;
        }

        String password = request.getHeader("x-robot-password");
        Authentication authRequest = RobotAuthentication.unauthenticated(password);
        try{
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContext newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request,response);
        }catch(AuthenticationException ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type","text/plain;charset=utf-8");
            response.getWriter().println(ex.getMessage());
        }
    }
}
