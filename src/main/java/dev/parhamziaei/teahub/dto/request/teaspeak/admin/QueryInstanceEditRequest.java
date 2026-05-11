package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.validation.annotation.IpAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryInstanceEditRequest extends QueryInstanceInitRequest {

}
