package base.ControllerWeb;

import base.Entity.Client;
import base.Service.SecurityService.MyClientDetails;
import base.Service.SecurityService.MyClientService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        MyClientDetails myClientDetails = (MyClientDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(myClientDetails.getClient().getClientId()))
                .claim("username", myClientDetails.getUsername())
                .claim("role", myClientDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            // Невалидный токен
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Истек срок действия
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Неподдерживаемый токен
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // Пустой claims
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    // Извлечение username из токена
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username", String.class);
    }

    // Дополнительно: извлечение ID пользователя
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
