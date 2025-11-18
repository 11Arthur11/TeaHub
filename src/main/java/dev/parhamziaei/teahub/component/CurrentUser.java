package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.entity.jpa.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@EqualsAndHashCode(callSuper = true)
@RequestScope
@Data
public class CurrentUser extends User {}
