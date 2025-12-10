package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.ProductType;
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
    private ProductType productType;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BillableProduct> products = new ArrayList<>();

    public void appendProduct(BillableProduct product) {
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
        this.productType = ProductType.EMPTY;
    }

    @PreUpdate
    private void preUpdate() {
        if (this.products.isEmpty())
            this.productType = ProductType.EMPTY;
    }

}
