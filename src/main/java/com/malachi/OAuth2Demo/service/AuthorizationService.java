package com.malachi.OAuth2Demo.service;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorizationService {
    private final List<String> allowedAdminNames = List.of("jane","jake","john","doe");

    public boolean  canViewAdminDashboard(Authentication authentication, String username){
        System.out.println("username passed: " + username);
        System.out.println("authentication name : " + authentication.getName());
        boolean result =  authentication.getName().contains("y") || username.equals("admin");
        System.out.println("result: " + result);
        return result;
    }

    public boolean canEditPage(Authentication authentication,String[] usernames){
        List<String> usernameList = List.of(usernames);
        System.out.println("username list: " + usernameList);
        return usernameList.contains(authentication.getName());
    }
}
