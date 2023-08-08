package me.sku.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.sku.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure() {//인증, 인가 서비스를 모든 곳에 적용하지 않겠다.
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//특정 http 요청에 웹 기반 보안을 구성함
        return http
                .authorizeRequests()//인증, 인가 설정 // "/login", "/signup", "/user" 해당 url 요청이 오면 인증/인가 없이도 접근가능
                    .requestMatchers("/login", "/signup", "/user").permitAll()
                    .anyRequest().authenticated()//
                .and()//폼 기반 로그인 설정
                .formLogin()
                    .loginPage("/login")//로그인페이지경로
                    .defaultSuccessUrl("/articles")//로그인 성공시 이동할 경로
                .and()//로그아웃설정
                .logout()
                    .logoutSuccessUrl("/login")//로그아웃시 이동할 경로
                    .invalidateHttpSession(true)//로그아웃 후 세션을 전체 삭제할지 여부설정
                .and()
                .csrf().disable()//Csrf 실습을 위한 비활성화
                .build();
    }

    @Bean//인증 관리자 관련 설정, 사용자 정보를 가져올 서비스 재정의 , 인증 밥법 등을 설정할 때 사용
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)//사용자 정보를 가져올 서비스를 설정하되, UserDetailsService를 상속받은 클래스여야 함
                .passwordEncoder(bCryptPasswordEncoder)//비밀번호를 암호화 하기 위한 인코더를 설정한다
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }//패스워드 인코더를 빈으로 등록
}
