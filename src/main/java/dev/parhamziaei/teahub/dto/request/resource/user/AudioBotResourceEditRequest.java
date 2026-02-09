package dev.parhamziaei.teahub.dto.request.resource.user;

import lombok.Data;

@Data
public class AudioBotResourceEditRequest {

    private String botNickname;

    private String serverAddress;

    private String serverPassword;

}
