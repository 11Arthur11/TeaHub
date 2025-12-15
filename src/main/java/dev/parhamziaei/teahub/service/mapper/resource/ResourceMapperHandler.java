package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import jakarta.transaction.Transactional;

public interface ResourceMapperHandler {

    ResourceType getType();

    @Transactional
    BaseResourceDetailResponse mapResourceDetailResponse(BillableResource resource);

    @Transactional
    BaseResourceDetailResponse mapResourceDetailAdminResponse(BillableResource resource);

}
