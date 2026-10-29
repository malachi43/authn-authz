package com.malachi.OAuth2Demo.providers;

import com.malachi.OAuth2Demo.authentication.RobotAuthentication;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class RobotAuthenticationProvider implements AuthenticationProvider {
    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RobotAuthentication authRequest = (RobotAuthentication) authentication;
        String password = authRequest.getPassword();
        if(!password.equals("malachi_12")){
            throw new BadCredentialsException("You're not Malachi Robot");
        }
        return RobotAuthentication.authenticated();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RobotAuthentication.class.isAssignableFrom(authentication);
    }
}
