package com.codestates.config;

import com.codestates.member.InMemoryMemberService;
import com.codestates.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration // Spring Security Configuration 적용 => 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class JavaConfiguration {
    @Bean
    public MemberService inMemoryMemberService(UserDetailsManager userDetailsManager,
                                               PasswordEncoder passwordEncoder) {
        return new InMemoryMemberService(userDetailsManager, passwordEncoder);
    }
}
