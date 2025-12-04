package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends BaseEntity<Long> {

    @Column(unique = true ,nullable = false, updatable = false, name = "token")
    private String token;

    @Column(nullable = false, updatable = false, name = "token_owner")
    private String tokenOwner;

    @Column(name = "active")
    private boolean active = true;

    public RefreshToken(String token, String tokenOwner) {
        this.token = token;
        this.tokenOwner = tokenOwner;
    }
}
