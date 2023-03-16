package com.codestates.config;

import com.codestates.member.DBMemberService;
import com.codestates.member.InMemoryMemberService;
import com.codestates.member.MemberRepository;
import com.codestates.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration // Spring Security Configuration 적용 => 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class JavaConfiguration {
    // 데이터베이스에 User의 정보를 저장하기 위해 MemberService 인터페이스의 구현 클래스를 DBMemberService로 변경
    @Bean
    public MemberService dbMemberService(MemberRepository memberRepository,
                                         PasswordEncoder passwordEncoder) {
        return new DBMemberService(memberRepository, passwordEncoder); // DI
    }
}
