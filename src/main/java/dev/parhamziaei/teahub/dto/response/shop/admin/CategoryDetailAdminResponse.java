package dev.parhamziaei.teahub.dto.response.shop.admin;

import lombok.Data;

@Data
public class CategoryDetailAdminResponse {

    private Long id;
    private String name;
    private boolean active;
    private String description;
    private String slug;

}
