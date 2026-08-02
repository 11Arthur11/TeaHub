package dev.parhamziaei.teahub.dto.response.system.admin;

import lombok.Builder;

import java.time.Instant;

@Builder
public record LogEvent(

        String logger,
        String level,
        String message,
        String thread,
        Instant timestamp

) {
}