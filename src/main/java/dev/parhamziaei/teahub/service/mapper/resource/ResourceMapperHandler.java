package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import jakarta.transaction.Transactional;

public interface ResourceMapperHandler {

    ResourceType getType();

    @Transactional
    AbstractResourceDetailResponse mapResourceDetailResponse(BillableResource resource);

    @Transactional
    AbstractResourceDetailResponse mapResourceDetailAdminResponse(BillableResource resource);

}
