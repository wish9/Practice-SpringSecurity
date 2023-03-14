package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration // Spring Security Configuration 적용 = 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class SecurityConfiguration {
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
