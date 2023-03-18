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
    private final HelloUserDetailsServiceV3 userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public HelloUserAuthenticationProvider(HelloUserDetailsServiceV3 userDetailsService,
                                           PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 가입을 하지 않고 로그인을 시도할 경우 Whitelebel Error Page 오류가 발생했다.
    // DBMemberService클래스의 verifyExistsEmail()메서드에서 등록된 회원 정보가 없으면, BusinessLogicException을 throw 하는데
    // 이 BusinessLogicException이 Cusotm AuthenticationProvider를 거쳐 그대로 Spring Security 내부 영역으로 throw 되기 때문이였다.

    // AuthenticationException을 rethrow 하도록 개선 authenticate()을 개선
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = authToken.getName();
        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String password = userDetails.getPassword();
            verifyCredentials(authToken.getCredentials(), password);

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
        } catch (Exception ex) {
            throw new UsernameNotFoundException(ex.getMessage()); // AuthenticationException으로 다시 throw
            // UsernameNotFoundException은 AuthenticationException을 상속하는 하위 Exception
        }
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