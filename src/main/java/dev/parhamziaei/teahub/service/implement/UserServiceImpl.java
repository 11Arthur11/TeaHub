package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.dto.request.query.UsersFilterRequest;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.user.AbstractUserDetailResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.RoleListResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.dto.response.user.admin.UserListResponse;
import dev.parhamziaei.teahub.entity.jpa.user.Role;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.UserSetting;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.user.Roles;
import dev.parhamziaei.teahub.exception.custom.authentication.PhoneNumberAlreadyTakenException;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.RoleRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.aggregate.UserRegistersMetricAggregate;
import dev.parhamziaei.teahub.repository.jpa.specification.UserSpecification;
import dev.parhamziaei.teahub.repository.redis.OnlineUserRedisRepo;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.service.mapper.UserMapStruct;
import dev.parhamziaei.teahub.utils.PersianPeriod;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final UserMapStruct userMapper;
    private final OnlineUserRedisRepo onlineUserRedisRepo;

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
    @Transactional
    public void register(String phoneNumber, RegisterRequest registerRequest) {
        if (!isPhoneNumberValid(registerRequest.getEmail())) {
            throw new PhoneNumberAlreadyTakenException();
        }

        Role loadedRole = roleRepository.findByName(Roles.ROLE_USER.value())
                .orElseThrow(() -> new NoSuchRoleException(Roles.ROLE_USER.value()));
        UserSetting userSetting = new UserSetting();
        Wallet wallet = new Wallet();

        User user = User.builder()
                .email(registerRequest.getEmail())
                .phone(phoneNumber)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();

        user.setRole(loadedRole);
        user.setWallet(wallet);
        user.setSetting(userSetting);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void userLocked(Long userId, boolean locked) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);
        user.setLocked(locked);
    }

    @Override
    @Transactional
    public void setRole(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchEntityException("Role not found with id: " + roleId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException("User not found with id: " + userId));

        user.setRole(role);
    }

    @Override
    public List<RoleListResponse> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(r -> modelMapper.map(r, RoleListResponse.class))
                .toList();
    }

    @Override
    public void editUser(Long userId, UserEditAdminRequest editRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchEntityException("User not found with id: " + userId));

        userMapper.updateEntity(editRequest, user);
        userRepository.save(user);
    }

    @Override
    public PagedModel<UserListResponse> getAllUsers(UsersFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Specification<User> spec = UserSpecification.byEnabled(filter.getByEnabled())
                .and(UserSpecification.byLocked(filter.getByLocked()))
                .and(UserSpecification.byRoleId(filter.getByRoleId()));

        Page<User> page = userRepository.findAll(spec, pageable);

        if (page.getContent().isEmpty())
            throw new NoSuchDataException();

        List<UserListResponse> mapped = page.getContent()
                .stream()
                .map(u -> {
                    UserListResponse res = modelMapper.map(u, UserListResponse.class);
                    res.setRole(messageService.get(Roles.fromName(u.getRole().getName())));
                    res.setFullName(u.getFullName());
                    res.setOnline(onlineUserRedisRepo.isOnline(u.getId()));
                    return res;
                })
                .toList();

        return new PagedModel<>(
                new PageImpl<>(
                        mapped,
                        pageable,
                        page.getTotalElements()
                )
        );
    }

    @Override
    public <T extends AbstractUserDetailResponse> T getProfile(Long userId, Class<T> clazz) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);
        T response = modelMapper.map(user, clazz);
        response.setOnline(onlineUserRedisRepo.isOnline(user.getId()));
        response.setRole(Roles.fromName(user.getRole().getName()));
        return response;
    }

    @Override
    public AdminMetric.UserMetric userMetric() {
        PersianPeriod.TimeRange today = PersianPeriod.today();
        PersianPeriod.TimeRange yesterday = PersianPeriod.yesterday();
        PersianPeriod.TimeRange thisMonth = PersianPeriod.thisMonth();
        PersianPeriod.TimeRange lastMonth = PersianPeriod.lastMonth();
        PersianPeriod.TimeRange thisWeek = PersianPeriod.thisWeek();
        PersianPeriod.TimeRange lastWeek = PersianPeriod.lastWeek();

        UserRegistersMetricAggregate current = userRepository.aggregateUserRegisters(
                today.start(),
                today.end(),

                thisWeek.start(),
                thisWeek.end(),

                thisMonth.start(),
                thisMonth.end()
        );

        UserRegistersMetricAggregate previous = userRepository.aggregateUserRegisters(
                yesterday.start(),
                yesterday.end(),

                lastWeek.start(),
                lastWeek.end(),

                lastMonth.start(),
                lastMonth.end()
        );

        return new AdminMetric.UserMetric(
                onlineUserRedisRepo.countAll(),
                new AdminMetric.PeriodComparison<>(current.daily().longValue(), previous.daily().longValue()),
                new AdminMetric.PeriodComparison<>(current.daily().longValue(), previous.daily().longValue()),
                new AdminMetric.PeriodComparison<>(current.daily().longValue(), previous.daily().longValue())
        );
    }

    @Override
    @Transactional
    public void updateLastLogin(String phoneNumber) {
        User user = loadUserByPhoneNumber(phoneNumber);
        user.setLastLogin(LocalDateTime.now().withNano(0));
    }

}
