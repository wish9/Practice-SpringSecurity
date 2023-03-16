package com.codestates.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelloAuthorityUtils {
    @Value("${mail.address.admin}") // application.yml에 추가한 프로퍼티를 가져오는 표현식 // 특정 이메일 주소에 관리자 권한을 부여할 수 있는지를 결정하기 위해 사용
    private String adminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"); // 관리자용 권한 목록 생성

    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER"); // 일반 사용 권한 목록 생성

    public List<GrantedAuthority> createAuthorities(String email) {
        if (email.equals(adminMailAddress)) { // 파라미터로 전달받은 이메일 주소가 application.yml 파일에서 가져온 관리자용 이메일 주소와 같다면
            return ADMIN_ROLES; // 관리자용 권한을 리턴
        }
        return USER_ROLES; // 관리자 이메일 목록에 없는 이메일이면 일반 사용자 권한을 리턴
    }
}
