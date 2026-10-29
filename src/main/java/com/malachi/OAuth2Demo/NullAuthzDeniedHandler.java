package com.malachi.OAuth2Demo;

import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NullAuthzDeniedHandler implements MethodAuthorizationDeniedHandler {
    @Override
    public @Nullable Object handleDeniedInvocation(MethodInvocation methodInvocation, AuthorizationResult authorizationResult) {
        //Execute this block of code when the authz decision fails.

        Map<String,Object> result = new HashMap<>();
        result.put("status", false);
        result.put("message", "Access Denied");
        List<String>  res = result.entrySet().stream().map(item -> {
            return item.getKey() + " : " + item.getValue();
        }).collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();
        builder.append("{ ");
        for(String str : res){
            builder.append(str).append(", ");
        }
        builder.append(" }");
        return builder.toString();
    }
}
