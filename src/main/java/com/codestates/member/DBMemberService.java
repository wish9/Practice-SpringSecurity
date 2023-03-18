package com.codestates.member;

import com.codestates.auth.utils.HelloAuthorityUtils;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class DBMemberService implements MemberService { // User의 인증 정보를 데이터베이스에 저장하는 역할

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HelloAuthorityUtils authorityUtils;

    public DBMemberService(MemberRepository memberRepository, // 생성자를 통해 MemberRepository와 PasswordEncoder Bean 객체를 DI
                           PasswordEncoder passwordEncoder, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        String encryptedPassword = passwordEncoder.encode(member.getPassword());  // PasswordEncoder를 이용해 패스워드를 암호화
        member.setPassword(encryptedPassword);    // 암호화된 패스워드를 password 필드에 다시 할당

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles); // Role을 DB에 저장하기 위해 추가

        Member savedMember = memberRepository.save(member);

        System.out.println("# Create Member in DB");
        return savedMember;
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS); // 회원 가입을 하지 않고 로그인을 시도할 경우 여기서 BusinessLogicException을 throw
    }
}
