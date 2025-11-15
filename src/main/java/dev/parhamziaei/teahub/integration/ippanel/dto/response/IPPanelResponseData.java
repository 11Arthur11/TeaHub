package dev.parhamziaei.teahub.integration.ippanel.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class IPPanelResponseData {
    List<Long> message_outbox_ids;
}
