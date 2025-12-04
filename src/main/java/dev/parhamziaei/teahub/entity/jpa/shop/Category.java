package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity<Long> {

    @Column(unique = true)
    private String name;

    private boolean active;

    private String description;

    @Column(unique = true)
    private String slug;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<BaseProduct> products = new ArrayList<>();

}
