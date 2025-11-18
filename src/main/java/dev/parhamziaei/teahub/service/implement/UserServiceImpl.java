package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.entity.jpa.Role;
import dev.parhamziaei.teahub.entity.jpa.User;
import dev.parhamziaei.teahub.entity.jpa.UserSetting;
import dev.parhamziaei.teahub.entity.jpa.Wallet;
import dev.parhamziaei.teahub.enums.Roles;
import dev.parhamziaei.teahub.exception.custom.authentication.PhoneNumberAlreadyTakenException;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.repository.jpa.RoleRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid phone number"));
    }

    @Override
    public User loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid phone number"));
    }

    @Override
    public boolean isUserRegistered(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void register(String phoneNumber, RegisterRequest registerRequest) {
        if (!isPhoneNumberValid(registerRequest.getEmail())) {
            throw new PhoneNumberAlreadyTakenException();
        }

        Role loadedRole = roleRepository.findByName(Roles.USER.value())
                .orElseThrow(() -> new NoSuchRoleException(Roles.USER.value()));
        UserSetting userSetting = new UserSetting();
        Wallet wallet = new Wallet();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .phone(phoneNumber)
                .roles(List.of(loadedRole))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();

        user.setWallet(wallet);
        user.setSetting(userSetting);
        userRepository.save(user);
    }

    @Override
    public void enableUser(String phoneNumber) {
        User user = loadUserByPhoneNumber(phoneNumber);
        user.setEnabled(true);
        userRepository.update(user);
    }

    @Override
    public void setRole(String phoneNumber, Roles role) {
        Role loadedRole = roleRepository.findByName(role.value())
                .orElseThrow(() -> new NoSuchRoleException(role.value()));
        List<Role> newRoleSet = List.of(loadedRole);
        User user = loadUserByPhoneNumber(phoneNumber);
        user.setRoles(newRoleSet);
        userRepository.update(user);
    }

    @Override
    public void updateLastLogin(String phoneNumber) {
        User user = loadUserByPhoneNumber(phoneNumber);
        user.setLastLogin(LocalDateTime.now().withNano(0));
        userRepository.update(user);
    }

}
