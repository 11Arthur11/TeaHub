package dev.parhamziaei.teahub.dto.request.resource.user;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.RequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@RequestSubType(ResourceType.TEASPEAK_RESOURCE)
public class NewTeaSpeakResourceRequest extends AbstractNewResourceRequest {}
