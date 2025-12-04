package dev.parhamziaei.teahub.configuration;

import dev.parhamziaei.teahub.component.filter.AlreadyLoggedInFilter;
import dev.parhamziaei.teahub.component.filter.JwtAuthFilter;
import dev.parhamziaei.teahub.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ApplicationContext applicationContext;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    // Note: This bean configures Spring Security to use a hierarchical role system, each role automatically inherits the permissions of the roles below it.
    @Bean
    public static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(Roles.ADMIN.nameWithoutPrefix()).implies(Roles.SUPPORT.nameWithoutPrefix())
                .role(Roles.SUPPORT.nameWithoutPrefix()).implies(Roles.USER.nameWithoutPrefix())
                .build();
    }

    // Note: This bean method imports role hierarchy setting into @PreAuthorize & @PostAuthorize annotations
    @Bean
    public static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    // Note: This bean method is main configuration of spring boot
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        final JwtAuthFilter jwtAuthFilter = applicationContext.getBean(JwtAuthFilter.class);
        final AlreadyLoggedInFilter alreadyLoggedInFilter = applicationContext.getBean(AlreadyLoggedInFilter.class);
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize -> authorize
                        .requestMatchers("/v1/auth/**", "/v1/payments/gateway/**").permitAll()
                        .requestMatchers("/docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/v1/admin/tickets/**").hasRole(Roles.SUPPORT.nameWithoutPrefix())
                        .requestMatchers("/v1/admin/**").hasRole(Roles.ADMIN.nameWithoutPrefix())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(alreadyLoggedInFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
