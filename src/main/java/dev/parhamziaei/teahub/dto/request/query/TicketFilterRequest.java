package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketFilterRequest extends AbstractPaginationRequest {

    @EnumValue(enumClass = TicketStatus.class, allowNull = true)
    protected String status;

    @EnumValue(enumClass = TicketDepartment.class, allowNull = true)
    protected String department;

    protected String sortedBy = "createdAt";
}


