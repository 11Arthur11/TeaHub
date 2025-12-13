package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.teaspeak.TeaSpeakResourceTokenResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin.TeaSpeakResourceDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.TeaSpeakResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.ResourceType;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.TeaSpeakService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeaSpeakResourceMapper implements ResourceMapperHandler {

    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final TeaSpeakService teaSpeakService;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK_RESOURCE;
    }

    private <T extends TeaSpeakResourceDetailResponse> T mapInternal(TeaSpeakResource resource, Class<T> clazz) {
        teaSpeakService.syncWithQuery(resource);
        T resourceDetail = modelMapper.map(resource, clazz);
        resourceDetail.setTeaSpeakStatus(messageService.get(resource.getTeaSpeakStatus()));
        resourceDetail.setResourceStatus(messageService.get(resource.getResourceStatus()));
        resourceDetail.setProductName(resource.getProduct().getProductName());
        resourceDetail.setPrivilegeToken(modelMapper.map(resource.getPrivilegeToken(), TeaSpeakResourceTokenResponse.class));
        return resourceDetail;
    }

    @Override
    public TeaSpeakResourceDetailResponse mapResourceDetailResponse(BillableResource resource) {
        TeaSpeakResource teaSpeakResource = (TeaSpeakResource) resource;
        return mapInternal(teaSpeakResource, TeaSpeakResourceDetailResponse.class);
    }

    @Override
    public TeaSpeakResourceDetailAdminResponse mapResourceDetailAdminResponse(BillableResource resource) {
        TeaSpeakResource teaSpeakResource = (TeaSpeakResource) resource;
        TeaSpeakResourceDetailAdminResponse dto = mapInternal(teaSpeakResource, TeaSpeakResourceDetailAdminResponse.class);
        dto.setOwnerPhone(resource.getOwner().getPhone());
        return dto;
    }


}
