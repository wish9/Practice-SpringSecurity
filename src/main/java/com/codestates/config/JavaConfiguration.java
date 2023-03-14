package com.codestates.config;

import com.codestates.member.InMemoryMemberService;
import com.codestates.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring Security Configuration 적용 = 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class JavaConfiguration {
    @Bean
    public MemberService inMemoryMemberService() {
        return new InMemoryMemberService();
    }
}
