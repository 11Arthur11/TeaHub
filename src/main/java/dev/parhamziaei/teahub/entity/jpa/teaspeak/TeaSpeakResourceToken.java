package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeaSpeakResourceToken extends BaseEntity<Long> {

    private Long queryId;
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tea_speak_resource_id")
    private TeaSpeakResource teaSpeakResource;

    public TeaSpeakResourceToken(Long queryId, String token) {
        this.queryId = queryId;
        this.token = token;
    }

}
