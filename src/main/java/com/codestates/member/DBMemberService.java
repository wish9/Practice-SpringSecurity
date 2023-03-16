package com.codestates.member;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DBMemberService implements MemberService { // 데이터베이스에 User를 등록하기 위한 클래스

    @Override
    public Member createMember(Member member) {
        return null;
    }
}
