package dev.parhamziaei.teahub.entity.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_settings")
public class UserSetting extends BaseEntity<Long> {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "sms_notification_enabled")
    private boolean smsNotification = true;

    @Column(name = "email_notification_enabled")
    private boolean emailNotification = true;

}
