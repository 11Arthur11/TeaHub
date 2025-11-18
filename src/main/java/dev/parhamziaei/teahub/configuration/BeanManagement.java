package dev.parhamziaei.teahub.configuration;

import dev.parhamziaei.teahub.configuration.properties.IPPanelProperties;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketMessageResponse;
import dev.parhamziaei.teahub.entity.jpa.Ticket;
import dev.parhamziaei.teahub.entity.jpa.TicketMessage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

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
        return mapper;
    }

}
