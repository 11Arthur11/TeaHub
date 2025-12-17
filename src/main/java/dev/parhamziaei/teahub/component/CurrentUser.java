package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
public class CurrentUser {

    private Long id;
    private String phone;
    private String firstName;
    private String lastName;
    private String ip;
    private Long walletId;

}
