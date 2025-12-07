package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.CategoryProductType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long> {

    @Column(unique = true)
    private String name;

    private boolean active;

    private String description;

    @Column(unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    private CategoryProductType productType;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BaseProduct> products = new ArrayList<>();

    public void appendProduct(BaseProduct product) {
        product.setCategory(this);
        products.add(product);
    }

    @Builder
    public Category(String name, boolean active, String description, String slug) {
        this.name = name;
        this.active = active;
        this.description = description;
        this.slug = slug;
        this.products = new ArrayList<>();
        this.productType = CategoryProductType.EMPTY;
    }

    @PreUpdate
    private void preUpdate() {
        if (this.products.isEmpty())
            this.productType = CategoryProductType.EMPTY;
    }

}
