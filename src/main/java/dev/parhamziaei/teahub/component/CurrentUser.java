package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@EqualsAndHashCode(callSuper = true)
@RequestScope
@Data
public class CurrentUser extends User {}
