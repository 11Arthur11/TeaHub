package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.entity.jpa.Role;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        return false;
    }

    @Override
    public boolean isUserRegistered(String phoneNumber) {
        return false;
    }

    @Override
    public void register(RegisterRequest registerRequest) {

    }

    @Override
    public void enableUser(String phoneNumber) {

    }

    @Override
    public void setRole(String phoneNumber, Role role) {

    }

    @Override
    public void updateLastLogin(String phoneNumber) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
