package com.codestates.auth;

import com.codestates.auth.utils.HelloAuthorityUtils;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.Member;
import com.codestates.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class HelloUserDetailsServiceV2 implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    public HelloUserDetailsServiceV2(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new HelloUserDetails(findMember);  // Custom UserDetails 클래스의 생성자로 findMember를 전달하면서 코드를 간결화
    }

    // Custom UserDetails 클래스
    private final class HelloUserDetails extends Member implements UserDetails { // Spring Security의 User 정보로 변환 + 권한 정보 생성 (캡슐화)
        HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getEmail());  // User의 권한 정보 생성 (캡슐화)
        }

        @Override
        public String getUsername() { // Spring Security에서 인식할 수 있는 username을 Member 클래스의 email 주소로 채우기 // null일 수 없음
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() { // 그 밖에 UserDetails 인터페이스의 추상 메서드를 구현한 부분은 일단 ture로 고정 (중점 포인트X)
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}
