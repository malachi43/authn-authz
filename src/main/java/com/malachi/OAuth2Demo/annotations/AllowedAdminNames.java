package com.malachi.OAuth2Demo.annotations;

import com.malachi.OAuth2Demo.NullAuthzDeniedHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//we are calling the canEditPage method in the 'AuthorizationService' class
@PreAuthorize("@authorizationService.canEditPage(authentication,'{usernames}')")
@HandleAuthorizationDenied(handlerClass = NullAuthzDeniedHandler.class)
public @interface AllowedAdminNames {
    String[] usernames() default "";
}
