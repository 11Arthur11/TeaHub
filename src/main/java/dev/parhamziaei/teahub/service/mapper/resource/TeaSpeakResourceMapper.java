package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.teaspeak.TeaSpeakResourceTokenResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin.TeaSpeakResourceDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.TeaSpeakResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.integration.teaspeak_query.dto.response.TSServerInfoResponse;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.TeaSpeakService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeaSpeakResourceMapper implements ResourceMapperHandler {

    private final ModelMapper modelMapper;
    private final TeaSpeakService teaSpeakService;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK;
    }

    private <T extends TeaSpeakResourceDetailResponse> T mapInternal(TeaSpeakResource resource, Class<T> clazz) {
        TSServerInfoResponse info = teaSpeakService.syncWithQuery(resource);
        T resourceDetail = modelMapper.map(resource, clazz);
        resourceDetail.setAddress(resource.getParentQueryInstance().getCredentials().ip());
        resourceDetail.setPeriod(resource.getProduct().getPeriod());
        resourceDetail.setProductName(resource.getProduct().getProductName());
        resourceDetail.setOnlineUsers(info.getOnlineUsers());
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
        dto.setOwnerId(resource.getOwner().getId());
        return dto;
    }


}
