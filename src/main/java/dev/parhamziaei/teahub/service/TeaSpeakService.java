package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeaSpeakService {

    private final CategoryRepository categoryRepo;
    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final QueryInstanceService queryInstanceService;

    @Transactional
    public void deployTeaSpeakInstance() {

    }

}
