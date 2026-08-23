package dev.parhamziaei.teahub.controller;

import dev.parhamziaei.teahub.component.AuthenticationFactory;
import dev.parhamziaei.teahub.component.CookieFactory;
import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.component.filter.AlreadyLoggedInFilter;
import dev.parhamziaei.teahub.component.filter.JwtAuthFilter;
import dev.parhamziaei.teahub.configuration.SecurityConfiguration;
import dev.parhamziaei.teahub.controller.global.PublicController;
import dev.parhamziaei.teahub.controller.global.TestController;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.service.CategoryService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.support.TestFixtures;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {TestController.class, PublicController.class})
@ContextConfiguration(classes = SecurityWebSliceTest.SliceConfiguration.class)
@Import({
        SecurityConfiguration.class,
        JwtAuthFilter.class,
        AlreadyLoggedInFilter.class,
        CurrentUser.class,
        TestController.class,
        PublicController.class
})
class SecurityWebSliceTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private JwtService jwtService;
    @MockitoBean private UserService userService;
    @MockitoBean private CookieFactory cookieFactory;
    @MockitoBean private AuthenticationFactory authenticationFactory;
    @MockitoBean private MessageService messageService;
    @MockitoBean private ProductService productService;
    @MockitoBean private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        when(categoryService.getAvailableCategories()).thenReturn(List.of());
    }

    @Test
    void publicEndpointDoesNotRequireJwt() throws Exception {
        mockMvc.perform(get("/v1/global/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointRejectsMissingJwt() throws Exception {
        mockMvc.perform(get("/v1/test/ip"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpointAcceptsValidJwt() throws Exception {
        authenticate(TestFixtures.user(1L, 10L, BigDecimal.ZERO));

        mockMvc.perform(get("/v1/test/ip"))
                .andExpect(status().isOk());
    }

    @Test
    void regularUserCannotAccessAdminRoute() throws Exception {
        authenticate(TestFixtures.user(1L, 10L, BigDecimal.ZERO));

        mockMvc.perform(get("/v1/admin/unknown"))
                .andExpect(status().isForbidden());
    }

    @Test
    void alreadyAuthenticatedUserCannotStartAnotherLogin() throws Exception {
        when(jwtService.extractJwtFromRequest(any(HttpServletRequest.class), eq(JwtType.ACCESS_TOKEN)))
                .thenReturn(Optional.of("access-token"));
        when(jwtService.isTokenValid("access-token", JwtType.ACCESS_TOKEN)).thenReturn(true);
        when(messageService.get(any(AuthMessage.class))).thenReturn("already logged in");

        mockMvc.perform(post("/v1/auth/login"))
                .andExpect(status().isConflict());
    }

    private void authenticate(User user) {
        when(jwtService.extractJwtFromRequest(any(HttpServletRequest.class), eq(JwtType.ACCESS_TOKEN)))
                .thenReturn(Optional.of("access-token"));
        when(jwtService.extractJwtFromRequest(any(HttpServletRequest.class), eq(JwtType.REFRESH_TOKEN)))
                .thenReturn(Optional.empty());
        when(jwtService.getPhoneNumber("access-token")).thenReturn(user.getPhone());
        when(jwtService.isTokenValid("access-token", JwtType.ACCESS_TOKEN)).thenReturn(true);
        when(userService.loadUserByPhoneNumber(user.getPhone())).thenReturn(user);
        doAnswer(invocation -> {
            User principal = invocation.getArgument(0);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
            );
            return null;
        }).when(authenticationFactory).buildAuthentication(eq(user), any(HttpServletRequest.class));
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class SliceConfiguration {
    }
}
