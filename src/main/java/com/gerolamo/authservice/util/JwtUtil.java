package com.gerolamo.authservice.util;

import com.gerolamo.authservice.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

  private final JwtConfig jwtConfig;

  public JwtUtil(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String generateToken(String username) {

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
        .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
        .compact();
  }

  public boolean validateToken(String token) {
      try {
          Jwts.parser()
                  .setSigningKey(jwtConfig.getSecret())
                  .build()
                  .parseClaimsJws(token);
          return true;
      } catch (Exception e) {
          return false;
      }
  }

  public String getUsernameFromToken(String token) {
      Claims body = Jwts.parser()
              .setSigningKey(jwtConfig.getSecret())
              .build()
              .parseClaimsJws(token)
              .getBody();

      return body.getSubject();
  }
}
