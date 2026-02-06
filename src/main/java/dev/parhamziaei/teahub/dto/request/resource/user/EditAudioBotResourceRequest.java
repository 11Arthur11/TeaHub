package dev.parhamziaei.teahub.dto.request.resource.user;

import dev.parhamziaei.teahub.validation.annotation.SafeName;
import lombok.Data;

@Data
public class EditAudioBotResourceRequest {

    private String botNickname;

    private String serverAddress;

    private String serverPassword;

}
