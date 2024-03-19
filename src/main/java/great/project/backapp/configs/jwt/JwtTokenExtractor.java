package great.project.backapp.configs.jwt;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenExtractor {

    private final TokenStore tokenStore;

    public JwtTokenExtractor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String getUserIdFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            OAuth2Authentication authentication = tokenStore.readAuthentication(token);
            if (authentication != null && authentication.getUserAuthentication() != null) {
                return authentication.getUserAuthentication().getName();
            }
        }
        return null;
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

