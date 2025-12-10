package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tea_speak_resource_id")
    private TeaSpeakResource teaSpeakResource;

    public TeaSpeakResourceToken(Long queryId, String token) {
        this.queryId = queryId;
        this.token = token;
    }

}
