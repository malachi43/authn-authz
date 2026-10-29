package com.malachi.OAuth2Demo.service;

import com.malachi.OAuth2Demo.annotations.AllowedAdminNames;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WebService {

    /*
    Complex expression can be passed into the "PreAuthorize" annotation.
    * */
    @PreAuthorize("""
    authentication.getName().contains('a') || 
    #username.equals('admin')
    """)
    public String test(String username){
        System.out.println("query params: " + username);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username: " + name.contains("a"));
        return "username contains a: " + name;
    }


    //You can call method from a class (the class must be a spring bean).
    @PreAuthorize("@authorizationService.canViewAdminDashboard(authentication,#username)")
    public String adminDashboard(String username){
        System.out.println("query params: " + username);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username: " + name.contains("a"));
        return "username contains a: " + name;
    }

    //You can use annotations as well.
    @AllowedAdminNames(usernames = {"john","jake","doe","mikey"})
    public String canEditPage(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return String.format("%s can edit the edit this page", name);
    }
}
