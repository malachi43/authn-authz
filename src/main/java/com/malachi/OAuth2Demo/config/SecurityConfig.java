package com.malachi.OAuth2Demo.config;

import com.malachi.OAuth2Demo.filter.RobotFilter;
import com.malachi.OAuth2Demo.providers.MalachiAuthenticationProvider;
import com.malachi.OAuth2Demo.providers.RateLimiterProvider;
import com.malachi.OAuth2Demo.providers.RobotAuthenticationProvider;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ProviderManager authManager = new ProviderManager(new RobotAuthenticationProvider());

    @Bean
    @Order(1)
    SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception{
         return http
                 .securityMatcher("/http-basic/**") //the filterchain will only be invoked/valid for path that match this pattern "/http-basic/**"
                 .authorizeHttpRequests(auth -> {
                     auth.anyRequest().authenticated();
                 })
                 .httpBasic(Customizer.withDefaults())
                 .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .build();
    }

    // This is the last filterchain, it catches all requets.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig.requestMatchers("/api/public").permitAll();
                    authorizeConfig.requestMatchers("/api/admin").hasRole("admin");

                    //only see your own profile
                    authorizeConfig.requestMatchers("/api/profile/{username}")
                            .hasVariable("username")
                            .equalTo(authentication -> authentication.getName());

                    //to perform fine-grained authorization checks.
                    authorizeConfig.requestMatchers("/no-a").access((authenticationSupplier, requestContext) -> {
                        return new AuthorizationDecision(false);
                    });

                    //can only be accessed through localhost
                    authorizeConfig.requestMatchers("/api/localhost")
                                    .access((_authenticationSupplier, requestContext)->{
                                        return new AuthorizationDecision(requestContext.getRequest().getServerName().equals("localhost"));
                                    });

                    authorizeConfig.anyRequest().authenticated();
                })
                .formLogin( (formConfig) -> {
                          formConfig.defaultSuccessUrl("/api/private");
                        }
                )
                .httpBasic(hbConfig -> {
                })
                .oauth2Login(oauth2Config -> {
                    oauth2Config.withObjectPostProcessor(
                            //The Generic parameter will be provided to us by spring security where we can wrap our own behaviour.
                            new ObjectPostProcessor<AuthenticationProvider>() {
                                @Override
                                public <O extends AuthenticationProvider> O postProcess(O object) {
                                    System.out.println("object in postprocessor: " + object);
                                    return (O) new RateLimiterProvider(object);
                                }
                            }
                    );
                })
                .addFilterBefore(new RobotFilter(authManager), AuthorizationFilter.class)
                .authenticationProvider(new MalachiAuthenticationProvider()) //Order matters,so our authentication provider will be executed first before delegating to spring security.
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("malachi")
                        .password("{noop}123456")
                        .authorities("ROLE_user")
                .build(),
                User.builder()
                        .username("mikey")
                        .password("{noop}123456")
                        .authorities("ROLE_user")
                        .build(),
                User.builder()
                        .username("john")
                        .password("{noop}123456")
                        .authorities("ROLE_user")
                        .build(),
                User.builder()
                        .username("jake")
                        .password("{noop}123456")
                        .authorities("ROLE_user")
                        .build()
        );
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> listener(){
        return event -> {
            System.out.println(String.format("Login successful ::: '%s' logged in with %s", event.getAuthentication().getName(), event.getAuthentication().getClass().getSimpleName()));
            event.getAuthentication().getName();
        };
    }


    //This helps to inject annotations
    @Bean
    AnnotationTemplateExpressionDefaults annotationTemplateExpressionDefaults(){
        return new AnnotationTemplateExpressionDefaults();
    }


}

class CustomAuthenticationConverter implements AuthenticationConverter{

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        return null;
    }
}
