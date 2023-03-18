package com.codestates.auth;

import com.codestates.auth.utils.HelloAuthorityUtils;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.Member;
import com.codestates.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloUserDetailsServiceV1 implements UserDetailsService {   // Custom UserDetailsService 구현 // DB에서 조회한 인증 정보를 기반으로 인증 처리하기 위한 커스텀 클래스
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    public HelloUserDetailsServiceV1(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository; // DB에서 User를 조회하기 위해 DI
        this.authorityUtils = authorityUtils; // 조회한 User의 권한(Role) 정보를 생성하기 위해 DI
    }

    // UserDetailsService 인터페이스를 implements 하는 구현 클래스는 loadUserByUsername(String username)이라는 추상 메서드를 구현해야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND)); // orElseThrow() => 값이 null이면 오류 던지는 메서드

        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail()); // Role 기반의 권한 정보(GrantedAuthority) 컬렉션을 생성

        return new User(findMember.getEmail(), findMember.getPassword(), authorities); // User의 인증 정보만 Spring Security에 전달 // 인증 처리는 Spring Security가 해줌
    }
}
