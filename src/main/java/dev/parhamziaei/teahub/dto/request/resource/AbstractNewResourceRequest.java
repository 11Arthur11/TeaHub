package dev.parhamziaei.teahub.dto.request.resource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.parhamziaei.teahub.dto.request.resource.user.NewTeaSpeakResourceRequest;
import dev.parhamziaei.teahub.enums.ResourceType;
import lombok.Data;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.EXISTING_PROPERTY,
//        property = "type"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = NewTeaSpeakResourceRequest.class, name = "TEASPEAK")
//})
@Data
public abstract class AbstractNewResourceRequest {

    public abstract ResourceType getResourceType();
    private Long productId;
    private String label;

}
