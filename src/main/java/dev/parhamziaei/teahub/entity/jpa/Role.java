package dev.parhamziaei.teahub.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity<Long> {

    @Column(nullable = false, length = 20, name = "role_name")
    private String name;

    @Column(nullable = false, name = "role_hierarchy")
    private Integer hierarchy;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(String name, Integer hierarchy) {
        this.name = name;
        this.hierarchy = hierarchy;
    }

}
