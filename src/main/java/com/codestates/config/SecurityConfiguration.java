package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring Security Configuration 적용 = 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class SecurityConfiguration {

    // HttpSecurity클래스 = HTTP 요청에 대한 보안 설정을 구성하기 위한 핵심 클래스
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()                 // CSRF 공격에 대한 Spring Security에 대한 설정을 비활성화 (로컬 환경에서 테스트하기 때문)
                .formLogin()                      // 기본적인 인증 방법을 폼 로그인 방식으로 지정
                .loginPage("/auths/login-form")   // 만들어 둔 커스텀 로그인 페이지를 사용하도록 설정
                .loginProcessingUrl("/process_login")    // 로그인 인증 요청을 수행할 요청 URL을 지정
                .failureUrl("/auths/login-form?error")   // 로그인 실패시 뜨게 할 화면 지정
                .and()                                   // Spring Security 보안 설정을 메서드 체인 형태로 구성
                .authorizeHttpRequests()                     // 클라이언트의 요청이 들어오면 접근 권한을 확인하겠다고 정의
                .anyRequest()                            // 클라이언트의 모든 요청에 대해
                .permitAll();                            // 접근을 허용

        return http.build();
    }
    @Bean
    public UserDetailsManager userDetailsService() {

        UserDetails userDetails = // user 정보를 포함하는 인터페이스
                User.withDefaultPasswordEncoder()    // 디폴트 패스워드 인코더를 이용해 사용자 패스워드를 암호화
                        .username("wjwee9@gmail.com") // username 설정
                        .password("1111")            // 비번 설정
                        .roles("USER")               // 역할을 USER로 설정
                        .build();

        return new InMemoryUserDetailsManager(userDetails); // 위에서 설정한 userDetails을 구현해서 UserDetailsManager객체로 만들어 리턴
    }
}