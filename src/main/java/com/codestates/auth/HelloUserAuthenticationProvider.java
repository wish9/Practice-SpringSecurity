package com.codestates.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloUserAuthenticationProvider implements AuthenticationProvider { // Custom AuthenticationProvider
    private final HelloUserDetailsServiceV1 userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public HelloUserAuthenticationProvider(HelloUserDetailsServiceV1 userDetailsService,
                                           PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException { // 사용자 인증 여부를 결정하는 로직을 담당하는 메서드
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;  // 입력 받은 authentication를 캐스팅하여 토큰 얻어 냄

        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // userDetailsService를 이용해 데이터베이스에서 해당 사용자를 조회

        String password = userDetails.getPassword();
        verifyCredentials(authToken.getCredentials(), password);    // 로그인 시 입력한 비번과 DB에 저장된 비번이 일치하는지 검증

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();  // 일치한다면, 사용자의 권한 정보를 생성

        return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities); // 인증 정보 반환
    }

    @Override
    public boolean supports(Class<?> authentication) { // Username/Password 인증 방식을 지원한다는 것을 Spring Security에 알려주는 역할
        return UsernamePasswordAuthenticationToken.class.equals(authentication); // true를 반환하면, Spring Security는 해당 클래스의 authenticate() 메서드를 호출해 인증을 진행
    }

    private void verifyCredentials(Object credentials, String password) {
        if (!passwordEncoder.matches((String)credentials, password)) {
            throw new BadCredentialsException("Invalid User name or User Password");
        }
    }
}