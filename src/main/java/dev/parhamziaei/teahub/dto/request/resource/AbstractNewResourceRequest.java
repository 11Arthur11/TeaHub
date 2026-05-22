package dev.parhamziaei.teahub.dto.request.resource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.dto.request.resource.user.NewAudioBotResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.NewTeaSpeakResourceRequest;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.validation.annotation.SafeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NewTeaSpeakResourceRequest.class, name = "TEASPEAK"),
        @JsonSubTypes.Type(value = NewAudioBotResourceRequest.class, name = "AUDIO_BOT")
})
@Data
public abstract class AbstractNewResourceRequest {

    private ResourceType type;

    @NotNull
    private Long productId;

    @SafeName
    private String label;
}
