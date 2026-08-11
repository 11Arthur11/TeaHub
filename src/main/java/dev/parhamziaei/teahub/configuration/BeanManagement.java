package dev.parhamziaei.teahub.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketMessageResponse;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABConnectSettingsResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABInstanceListResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.mixin.*;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListDetailResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListItemResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListsResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanManagement {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(TicketMessage.class, TicketMessageResponse.class)
                .addMappings(m -> m.skip(TicketMessageResponse::setAttachments));
        mapper.typeMap(Ticket.class, TicketDetailBaseResponse.class)
                .addMappings(m -> m.skip(TicketDetailBaseResponse::setMessages));
        mapper.typeMap(Ticket.class, TicketDetailAdminResponse.class)
                .addMappings(m -> m.map(ticket -> ticket.getOwner().getId(), TicketDetailAdminResponse::setOwnerId))
                .addMappings(m -> m.map(ticket -> ticket.getOwner().getFullName(), TicketDetailAdminResponse::setOwnerFullName));
        return mapper;
    }

}
