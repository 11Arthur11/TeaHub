package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.system.SystemNotificationRequest;
import dev.parhamziaei.teahub.dto.response.system.user.SystemNotificationUserResponse;
import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.SystemNotificationRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.service.mapper.SystemNotificationMapStruct;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SystemNotificationRepository systemNotificationRepo;
    private final UserRepository userRepo;
    private final SystemNotificationMapStruct systemNotificationMapStruct;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public void sendGlobalNotification(Long publisherId, SystemNotificationRequest request) {
        SystemNotification notification = SystemNotification.builder()
                .title(request.getTitle())
                .text(request.getText())
                .createdAt(LocalDateTime.now().withNano(0))
                .expiresAt(request.getExpiresAt())
                .publisher(userRepo.findById(publisherId).orElseThrow(NoSuchEntityException::new))
                .build();

        systemNotificationRepo.save(notification);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void editGlobalNotification(SystemNotificationRequest request, Long notificationId) {
        SystemNotification notification = systemNotificationRepo.findById(notificationId)
                .orElseThrow(NoSuchEntityException::new);
        systemNotificationMapStruct.toEntity(request, notification);
        systemNotificationRepo.save(notification);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGlobalNotification(Long notificationId) {
        systemNotificationRepo.deleteById(notificationId);
    }

    public <T extends SystemNotificationUserResponse> List<T> getGlobalNotifications(Class<T> clazz) {
        List<T> response = systemNotificationRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(SystemNotification::getCreatedAt).reversed())
                .map(n -> modelMapper.map(n, clazz))
                .toList();

        if (response.isEmpty())
            throw new NoSuchDataException();

        return response;
    }



}
