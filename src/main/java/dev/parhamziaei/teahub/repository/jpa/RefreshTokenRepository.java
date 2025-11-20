package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.user.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    void update(RefreshToken refreshToken);

    void delete(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

}
