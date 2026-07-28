package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.repository.redis.OnlineUserRedisRepo;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationFactory {

    private final UserService userService;
    private final OnlineUserRedisRepo onlineUserRedisRepo;

    public void buildAuthentication(String phoneNumber, HttpServletRequest request) {
        User userDetails = (User) userService.loadUserByUsername(phoneNumber);
        buildAuthentication(userDetails, request);
    }

    public void buildAuthentication(User userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        userService.updateLastLogin(userDetails.getUsername());
        onlineUserRedisRepo.save(userDetails.getId());
        // reminder add log for logins here !!!
    }

}
