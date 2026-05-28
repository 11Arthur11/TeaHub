package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.enums.user.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

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

    public void setRole(Role role) {
        this.role = role;
        role.addUser(this);
    }

    public void setSetting(UserSetting setting) {
        this.setting = setting;
        setting.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }
    
    public boolean isAdmin() {
        System.out.println("user " + this.getUsername() + " role " + this.getRole().getName() + " isAdmin?" + this.role.getName().equals(Roles.ADMIN.name())                    );
        return this.role.getName().equals(Roles.ADMIN.name());
    }

    public boolean isStaff() {
        List<String> staffRoles = Roles.staffRoles().stream()
                .map(Roles::value)
                .toList();

        return staffRoles.contains(this.role.getName());
    }

    public Role getHigherAuthority() {
        return this.role;
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
