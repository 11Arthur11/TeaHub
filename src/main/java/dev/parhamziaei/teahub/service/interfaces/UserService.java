package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.entity.jpa.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean isPhoneNumberValid(String phoneNumber);
    boolean isUserRegistered(String phoneNumber);
    void register(RegisterRequest registerRequest);
    void enableUser(String phoneNumber);
    void setRole(String phoneNumber, Role role);
    void updateLastLogin(String phoneNumber);

}
