package dev.parhamziaei.teahub.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPresentation {

    private String description;

    @Column(columnDefinition = "json")
    private String features;

    @Column(columnDefinition = "json")
    private String badges;

}
