package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.ProductsType;
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
    private ProductsType productsType;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BaseProduct> products;

}
