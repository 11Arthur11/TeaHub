package dev.parhamziaei.teahub.dto.request.resource.user;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.ResourceRequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ResourceRequestSubType(ResourceType.TEASPEAK)
public class NewTeaSpeakResourceRequest extends AbstractNewResourceRequest {}
