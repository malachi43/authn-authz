package com.malachi.OAuth2Demo;

import com.malachi.OAuth2Demo.service.WebService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    public final WebService service;

    @GetMapping("/public")
    public ResponseEntity<String> publicApi(){

        return ResponseEntity.ok("This is a public API");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateApi() throws InterruptedException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("This is a private API(Authenticated)\n");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminApi() throws InterruptedException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("This is a admin API(Authenticated)\n");
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<String> profile(@PathVariable String username) throws InterruptedException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username: " + user);
        return ResponseEntity.ok("This is a profile for " + user);
    }

    @GetMapping("/localhost")
    public ResponseEntity<String> localhost() throws InterruptedException {
        return ResponseEntity.ok("This page was accessed through localhost.");
    }

    @GetMapping("/test")
    public String test(@RequestParam("name") String username){
      return service.test(username);
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@RequestParam(value = "name", required = false) String name){
      return service.adminDashboard(name == null ? "" : name);
    }

    @GetMapping("/edit")
    public String editPage(){
        return service.canEditPage();
    }
}
