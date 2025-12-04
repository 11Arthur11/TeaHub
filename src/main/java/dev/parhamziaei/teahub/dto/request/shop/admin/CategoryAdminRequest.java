package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.validation.annotation.Slug;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryAdminRequest {

    @Length(min = 5, max = 30)
    private String name;

    private boolean active;

    @Length(min = 5, max = 30)
    private String description;

    @Slug(allowNull = true)
    private String slug;

}
