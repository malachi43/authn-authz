package com.malachi.OAuth2Demo.providers;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterProvider implements AuthenticationProvider {
    private final AuthenticationProvider delegate;
    private final Map<String, Instant> cache = new ConcurrentHashMap<>();

    public RateLimiterProvider(AuthenticationProvider delegate){
        this.delegate = delegate;
    }
    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

         Authentication parentAuth = delegate.authenticate(authentication);
         if(parentAuth == null){
             return null;
         }

         if(isWithinLimit(parentAuth)){
             return parentAuth;
         }
         throw new BadCredentialsException("You cannot carry out successive login with 1 MINUTE");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }

    public boolean isWithinLimit(Authentication authentication){
        String rateLimitKey = authentication.getName();
        Instant now = Instant.now();
        Instant previousInstant = cache.get(rateLimitKey);

        if(previousInstant == null || previousInstant.plus(Duration.ofMinutes(1)).isBefore(now)){
            cache.put(rateLimitKey, now);
            return true;
        }
        return false;
    }
}
