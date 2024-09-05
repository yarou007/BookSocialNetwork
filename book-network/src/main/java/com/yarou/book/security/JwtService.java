package com.yarou.book.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  // besh ygenerati el token, ydecodi el token , w ycodi el token , extracti l infos , services mtaa el token el kol


    @Value("${application.security.jwt.expiration}")

    private Long jwtExpirtation ;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(UserDetails userDetails){
         return generateToken(new HashMap<>(), userDetails);
    }

    // this method
    public String generateToken(Map<String,Object> claims, UserDetails userDetails) {
        return buildToken(claims,userDetails,jwtExpirtation);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails, Long jwtExpirtation) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirtation))
                .claim("authorities",authorities)
                .signWith(getSignKey())
                .compact(); // generate token 
    }

    //decode sign sign key for token
    private Key getSignKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractUsername(String token) {
         return extractClaim(token, Claims::getSubject);
    }


    // used el extract all claims behs yjib ay info yheb aaleha ( tetaada fil param -> claimsReslover  )
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
          final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }


    //Jeb l info l kol li fil body
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
          final String username = extractUsername(token);
          return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
         return    extractClaim(token, Claims::getExpiration);
    }
}
