package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.authentication.ChangePasswordRequest;
import dev.parhamziaei.teahub.dto.request.authentication.ForgotPasswordRequest;
import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.entity.jpa.Role;
import dev.parhamziaei.teahub.entity.redis.ForgotPasswordSession;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean isPhoneNumberValid(String phoneNumber);
    void register(RegisterRequest registerRequest);
    void enableUser(String phoneNumber);
    void setRole(String phoneNumber, Role role);
    void updateLastLogin(String phoneNumber);
    void changePassword(ChangePasswordRequest changePasswordRequest, String phoneNumber);
    void changeForgottenPassword(ForgotPasswordRequest forgotPasswordRequest, ForgotPasswordSession forgotPasswordSession);

}
