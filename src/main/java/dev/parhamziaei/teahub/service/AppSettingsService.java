package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.system.ApplicationSettingDto;
import dev.parhamziaei.teahub.entity.jpa.ApplicationSettings;
import dev.parhamziaei.teahub.repository.jpa.ApplicationSettingRepository;
import dev.parhamziaei.teahub.service.mapper.AppSettingsMapStruct;
import dev.parhamziaei.teahub.service.mapper.InvoicePropertiesMapStruct;
import dev.parhamziaei.teahub.service.mapper.ProductPeriodSettingsMapStruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AppSettingsService {

    private final ModelMapper modelMapper;
    private final AppSettingsMapStruct appSettingMapStruct;
    private final ApplicationSettingRepository applicationSettingRepository;
    private final ProductPeriodSettingsMapStruct productPeriodSettingsMapStruct;
    private final InvoicePropertiesMapStruct invoicePropertiesMapStruct;

    public ApplicationSettingDto save(ApplicationSettingDto dto) {
        ApplicationSettings settings = applicationSettingRepository.find();
        productPeriodSettingsMapStruct.update(dto.getProductPeriodSettings(), settings.getProductPeriodSettings());
        invoicePropertiesMapStruct.update(dto.getInvoiceProperties(), settings.getInvoiceProperties());
        return modelMapper.map(
                applicationSettingRepository.save(settings),
                ApplicationSettingDto.class
        );
    }

    public ApplicationSettingDto get() {
        return modelMapper.map(applicationSettingRepository.find(), ApplicationSettingDto.class);
    }

}
