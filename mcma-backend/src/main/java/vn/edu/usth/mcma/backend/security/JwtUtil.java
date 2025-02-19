package vn.edu.usth.mcma.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.edu.usth.mcma.backend.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static vn.edu.usth.mcma.backend.config.AppConfig.dotenv;

@Component
public class JwtUtil {
    private static final String JWT_SECRET_KEY = dotenv().get("JWT_SECRET_KEY");
    private static final long ACCESS_EXPIRATION_TIME = Long.parseLong(Objects.requireNonNull(dotenv().get("ACCESS_EXPIRATION_TIME")));
    private static final long REFRESH_EXPIRATION_TIME = Long.parseLong(Objects.requireNonNull(dotenv().get("REFRESH_EXPIRATION_TIME")));
    private static final String USER_ID_CLAIM = "userId";
    private static final String ROLES_CLAIM = "roles";

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, ACCESS_EXPIRATION_TIME);
    }
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, REFRESH_EXPIRATION_TIME);
    }
    private String buildToken(UserDetails userDetails, long expirationTime) {
        User user = (User) userDetails;
        long now = System.currentTimeMillis();
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .claim(USER_ID_CLAIM, user.getId())
                .claim(ROLES_CLAIM, user.getRoles())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaimsFromToken(token).getSubject();
    }
    public Long extractUserId(String token) {
        return extractClaimsFromToken(token).get(USER_ID_CLAIM, Long.class);
    }
    public List<?> extractRoles(String token) {
        return extractClaimsFromToken(token).get(ROLES_CLAIM, List.class);
    }
    public Date extractIssuedDate(String token) {
        return extractClaimsFromToken(token).getIssuedAt();
    }
    public Date extractExpirationDate(String token) {
        return extractClaimsFromToken(token).getExpiration();
    }

    /**
     * can validate access token
     * @param token from http request
     * @param userDetails represent user
     * @return true if subject of token is user and token is not expired; else false
     */
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    /**
     * @param token the token
     * @throws io.jsonwebtoken.ExpiredJwtException this calls .parseClaimsJws() that throw the exception automatically
     * @return Claims
     */
    private Claims extractClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
