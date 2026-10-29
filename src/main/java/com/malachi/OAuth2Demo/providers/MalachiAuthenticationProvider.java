package com.malachi.OAuth2Demo.providers;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;


/*
* You can add this "MalachiAuthenticationProvider" to the security filter chain by using the authenticationProvider method in the HttpSecurity argument passed to the securityFilterChain method e.g [ httpSecurity.authenticationProvider(new MalachiAuthenticationProvider()) ]
* An AuthenticationProvider returns 3 things:
* 1. An authenticated object,
* 2. Throw an exception and
* 3. Return null (in cases where we delegate the authentication process to another AuthenticationProvider)

 */


public class MalachiAuthenticationProvider implements AuthenticationProvider {
    private final String ROLE_PREFIX = "ROLE_";
    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        if(username.equals("Malachi")){
            return UsernamePasswordAuthenticationToken.authenticated(username, null, AuthorityUtils.createAuthorityList(ROLE_PREFIX + "admin"));
        }
        return null; //we return null because we want another AuthenticationProvider to handle this.
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication); //this will handle token of type "UsernamePasswordAuthenticationToken"
    }
}
