package com.codestates.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelloAuthorityUtils {
    @Value("${mail.address.admin}") // application.yml에 추가한 프로퍼티를 가져오는 표현식 // 특정 이메일 주소에 관리자 권한을 부여할 수 있는지를 결정하기 위해 사용
    private String adminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"); // 관리자용 권한 목록 생성

    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER"); // 일반 사용 권한 목록 생성

    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");

    public List<GrantedAuthority> createAuthorities(String email) { // application.yml 파일 기반으로 권한 정보 생성
        if (email.equals(adminMailAddress)) { // 파라미터로 전달받은 이메일 주소가 application.yml 파일에서 가져온 관리자용 이메일 주소와 같다면
            return ADMIN_ROLES; // 관리자용 권한을 리턴
        }
        return USER_ROLES; // 관리자 이메일 목록에 없는 이메일이면 일반 사용자 권한을 리턴
    }

    public List<GrantedAuthority> createAuthorities(List<String> roles) { // DB에 저장된 Role을 기반으로 권한 정보 생성
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        // SimpleGrantedAuthority 객체를 생성할 때 생성자 파라미터로 넘겨주는 값이 “ USER" 또는 “ADMIN"으로 넘겨주면 안 되고 “ROLE_USER" 또는 “ROLE_ADMIN" 형태로 넘겨주어야 한다.
    }

    public List<String> createRoles(String email) { // DB 저장용
        if (email.equals(adminMailAddress)) {
            return ADMIN_ROLES_STRING;
        }
        return USER_ROLES_STRING;
    }

}
