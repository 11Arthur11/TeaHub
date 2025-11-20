package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.entity.jpa.user.User;

public interface NotificationService {

    <U extends User> void sendLoginNotificationLogin(U user);
}
