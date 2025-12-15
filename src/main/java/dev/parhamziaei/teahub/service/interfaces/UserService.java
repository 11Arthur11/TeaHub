package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.Roles;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean isPhoneNumberValid(String phoneNumber);
    User loadUserByPhoneNumber(String phoneNumber);
    boolean isUserRegistered(String phoneNumber);
    void register(String phoneNumber, RegisterRequest registerRequest);
    void enableUser(String phoneNumber);
    void setRole(String phoneNumber, Roles role);
    void updateLastLogin(String phoneNumber);

}
