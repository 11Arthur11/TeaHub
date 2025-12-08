package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<Long> implements UserDetails {

    @Column(unique = true, nullable = false, length = 100, name = "phone")
    private String phone;

    @Column(nullable = false, length = 100, name = "first_name")
    private String firstName;

    @Column(nullable = false, length = 100, name = "last_name")
    private String lastName;

    @Column(nullable = false, length = 80, name = "email")
    private String email;

    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSetting setting;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @Column(columnDefinition = "TIMESTAMP(0)", name = "last_login")
    private LocalDateTime lastLogin;

    @Column(columnDefinition = "TIMESTAMP(0)", name = "created_at")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP(0)", name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "expired")
    private boolean expired = false;

    @Column(name = "locked")
    private boolean locked = false;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired = false;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().withNano(0);
        this.updatedAt = LocalDateTime.now().withNano(0);
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().withNano(0);
    }

    public void setWallet(Wallet wallet) {
        wallet.setOwner(this);
        this.wallet = wallet;
    }

    public void setSetting(UserSetting setting) {
        this.setting = setting;
        setting.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority) role::getName).toList();
    }

    public boolean isStaff() {
        Set<String> userRoles = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        List<String> staffRoles = Roles.staffRoles().stream()
                .map(Roles::value)
                .toList();

        for (String userRole : userRoles) {
            if (staffRoles.contains(userRole)) {
                return true;
            }
        }
        return false;
    }

    public Role getHigherAuthority() {
        return roles.stream().max(Comparator.comparing(Role::getHierarchy)).get();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
