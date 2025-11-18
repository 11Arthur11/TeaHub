package dev.parhamziaei.teahub.controller.global;

import dev.parhamziaei.teahub.service.interfaces.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/test")
public class TestController {

    private final JwtService jwtService;

    @GetMapping("/ip")
    public ResponseEntity<?> getIp(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(request.getRemoteAddr());
    }

}
