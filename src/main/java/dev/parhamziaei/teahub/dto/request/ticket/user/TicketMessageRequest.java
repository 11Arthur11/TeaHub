package dev.parhamziaei.teahub.dto.request.ticket.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketMessageRequest {

    private String content;

    @JsonIgnore
    private List<MultipartFile> files;

    public void setFiles(List<MultipartFile> files) {
        this.files = Objects.requireNonNullElseGet(files, ArrayList::new);
    }
}
