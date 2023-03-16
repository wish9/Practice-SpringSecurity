package com.codestates.member;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

    // @Service을 사용하지 않고, JavaConfiguration을 이용해 Bean을 등록
    public class InMemoryMemberService implements MemberService {  //  InMemory User를 등록하기 위한 MemberService 인터페이스의 구현 클래스
        private final UserDetailsManager userDetailsManager;
        private final PasswordEncoder passwordEncoder;

        // UserDetailsManager와 PasswordEncoder를 의존성 부여
        public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
            this.userDetailsManager = userDetailsManager; // Spring Security의 User를 관리하는 관리자 역할
            this.passwordEncoder = passwordEncoder; // Spring Security User를 등록할 때 패스워드를 암호화해 주는 역할
        }

        public Member createMember(Member member) {
            List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name()); //  User의 권한 목록을 List<GrantedAuthority>로 생성

            String encryptedPassword = passwordEncoder.encode(member.getPassword()); // 등록할 User의 패스워드를 암호화

            UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities); // 위에서 선언한 정보들을 담아서 하나의 UserDetails로 만들기

            userDetailsManager.createUser(userDetails); // User 등록

            return member;
        }

        private List<GrantedAuthority> createAuthorities(String... roles) {
            //  Spring Security에서는 SimpleGrantedAuthority 를 사용해 Role 베이스 형태의 권한을 지정할 때 ‘ROLE_’ + 권한 명 형태로 지정해 주어야 한다. 그렇지 않을 경우 적절한 권한 매핑이 이루어지지 않는다.
            return Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        }
    }
