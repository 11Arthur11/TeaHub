package dev.parhamziaei.teahub.dto.response.global;

public record RedirectResponse(String redirectUrl) {
    public RedirectResponse withRedirectUrl(String redirectUrl) {
        return new RedirectResponse(redirectUrl);
    }
}
