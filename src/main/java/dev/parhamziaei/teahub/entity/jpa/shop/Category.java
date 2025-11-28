package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long> {

    private String name;

    private boolean active;

    private String description;

    private String slug;

    @Enumerated(EnumType.STRING)
    private CategoryType productsType;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BaseProduct> products;

}
