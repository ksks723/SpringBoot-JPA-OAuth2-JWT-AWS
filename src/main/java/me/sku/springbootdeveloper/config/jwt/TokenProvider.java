package me.sku.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.sku.springbootdeveloper.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {//토큰 만들기
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)//헤더:typ:jwt,내용 iss : 프로퍼티즈에서 설정한 값
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now) // 현재시간
                .setExpiration(expiry)//내용(만료시간)
                .setSubject(user.getEmail())//유저 이메일
                .claim("id", user.getId())//유저 아이디
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())//서명 : 비밀값과 함께 해시값을 hs256방식으로 암호화
                .compact();
    }

    public boolean validToken(String token) {//jwt 토큰 유효성 검증 메서드
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀값으로 복호화
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {//복호화에서 에러가 난다는건 유효하지 않은 토큰이라는것!
            return false;
        }
    }


    public Authentication getAuthentication(String token) {//토큰 기반으로 인증 정보를 가져오는 메서드 (토큰을 받아 인증정보를 담은 객체 Authentication을 반환한다.)
        Claims claims = getClaims(token);//프로퍼티즈 파일에 저장한 비밀 값으로 토큰을 복호화 한 뒤 클레임을 가져오는 private 메서드다.
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(//프로젝트 User클래스가 아닌 , 스프링 시큐리티에서 제공하는 객체인 User 클래스를 임포트 해야 한다.
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject()
                        , "", authorities), token, authorities
        );
    }

    public Long getUserId(String token) {//토큰 기반으로 유저 ID를 가져오는 메서드
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);//클레임에서 id 키로 저장된 값을 가져와 반환한다.
    }

    private Claims getClaims(String token) {
        return Jwts.parser()//클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
