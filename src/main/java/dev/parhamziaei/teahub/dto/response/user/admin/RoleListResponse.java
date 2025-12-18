package dev.parhamziaei.teahub.dto.response.user.admin;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RoleListResponse {
    private Long id;
    private String name;
    private Integer hierarchy;
}
