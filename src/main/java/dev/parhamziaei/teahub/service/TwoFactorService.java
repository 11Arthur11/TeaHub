package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.SessionProperties;
import dev.parhamziaei.teahub.entity.redis.PhoneVerifySession;
import dev.parhamziaei.teahub.entity.redis.TwoFactorSession;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.exception.custom.authentication.InvalidTwoFactorException;
import dev.parhamziaei.teahub.integration.ippanel.IPPanelService;
import dev.parhamziaei.teahub.repository.redis.PhoneVerifyRepo;
import dev.parhamziaei.teahub.repository.redis.TwoFactorRepo;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final TwoFactorRepo twoFactorRepo;
    private final PhoneVerifyRepo phoneVerifyRepo;
    private final IPPanelService smsService;
    private final SessionProperties sessionProperties;

    public void verifyTwoFactorSession(String twoFactorToken, String code) {
        String sessionId = jwtService.extractSessionId(twoFactorToken);
        TwoFactorSession session = twoFactorRepo.get(sessionId);
        if (jwtService.isTokenValid(twoFactorToken, JwtType.TWO_FACTOR_TOKEN) && session != null) {
            if (
                    session.getCode().equals(code) && //REMINDER: FOR DEV PHASE ONLY !!!!!
//                    encoder.matches(code, session.getCode()) &&
                    session.getAttempts() < 3 &&
                    session.getPhoneNumber().equals(jwtService.getPhoneNumber(twoFactorToken))
            ) {
                session.setVerified(true);
                twoFactorRepo.remove(sessionId);
                return;
            } else {
                session.setAttempts(session.getAttempts() + 1);
                twoFactorRepo.save(sessionId, session);
            }
            throw new InvalidTwoFactorException("TwoFactor code verification failed for: " + session.getPhoneNumber());
        }
        throw new InvalidTwoFactorException("Invalid session or session token");
    }

    public void verifyPhoneVerifySession(String phoneVerifyToken, String code) {
        String sessionId = jwtService.extractSessionId(phoneVerifyToken);
        PhoneVerifySession session = phoneVerifyRepo.get(sessionId);
        if (jwtService.isTokenValid(phoneVerifyToken, JwtType.PHONE_VERIFY_TOKEN) && session != null) {
            if (
                    session.getCode().equals(code) && //REMINDER: FOR DEV PHASE ONLY !!!!!
//                    encoder.matches(code, session.getCode()) &&
                    session.getAttempts() < 3 &&
                    session.getPhoneNumber().equals(jwtService.getPhoneNumber(phoneVerifyToken))
            ) {
                phoneVerifyRepo.remove(sessionId);
                return;
            } else {
                session.setAttempts(session.getAttempts() + 1);
                phoneVerifyRepo.save(sessionId, session);
            }
            throw new InvalidTwoFactorException("PhoneVerify code verification failed for: " + session.getPhoneNumber());
        }
        throw new InvalidTwoFactorException("Invalid session or session token");
    }

    public boolean hasActiveTwoFactorSession(String twoFactorToken) {
        String sessionId = jwtService.extractSessionId(twoFactorToken);
        return twoFactorRepo.get(sessionId) != null;
    }

    public boolean hasActivePhoneVerifySession(String phoneVerifyToken) {
        String sessionId = jwtService.extractSessionId(phoneVerifyToken);
        return phoneVerifyRepo.get(sessionId) != null;
    }

    public String sendTwoFactor(String phoneNumber) {
        SecureRandom random = new SecureRandom();
        TwoFactorSession session = TwoFactorSession.builder()
                .code(String.format("%06d", random.nextInt(100000)))
                .phoneNumber(phoneNumber)
                .verified(false)
                .build();

        String sessionId = UUID.randomUUID().toString();
//        smsService.sendTwoFactorSMS(session.getCode(), session.getPhoneNumber()); //REMINDER: FOR DEV PHASE ONLY !!!!!
//        session.setCode(encoder.encode(session.getCode())); //REMINDER: FOR DEV PHASE ONLY !!!!!
        twoFactorRepo.save(sessionId, session, sessionProperties.twoFactorSessionTtl());
        return sessionId;
    }

    public String sendPhoneVerify(String phoneNumber) {
        SecureRandom random = new SecureRandom();
        PhoneVerifySession session = PhoneVerifySession.builder()
                .code(String.format("%06d", random.nextInt(100000)))
                .phoneNumber(phoneNumber)
                .verified(false)
                .build();

        String sessionId = UUID.randomUUID().toString();
//        smsService.sendTwoFactorSMS(session.getCode(), session.getPhoneNumber()); //REMINDER: FOR DEV PHASE ONLY !!!!!
//        session.setCode(encoder.encode(session.getCode())); //REMINDER: FOR DEV PHASE ONLY !!!!!
        phoneVerifyRepo.save(sessionId, session, sessionProperties.twoFactorSessionTtl());
        return sessionId;
    }


}
