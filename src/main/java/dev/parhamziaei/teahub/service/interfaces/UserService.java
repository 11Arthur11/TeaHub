package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.dto.request.query.UsersFilterRequest;
import dev.parhamziaei.teahub.dto.response.user.AbstractUserDetailResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.RoleListResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.dto.response.user.admin.UserListResponse;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.Roles;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean isPhoneNumberValid(String phoneNumber);
    User loadUserByPhoneNumber(String phoneNumber);
    boolean isUserRegistered(String phoneNumber);
    void register(String phoneNumber, RegisterRequest registerRequest);
    void userLocked(Long userId, boolean locked);
    void setRole(Long userId, Long roleId);
    List<RoleListResponse> getRoles();
    void editUser(Long userId, UserEditAdminRequest editRequest);
    void updateLastLogin(String phoneNumber);
    PagedModel<UserListResponse> getAllUsers(UsersFilterRequest filter);
    <T extends AbstractUserDetailResponse> T getProfile(Long userId, Class<T> clazz);

}
