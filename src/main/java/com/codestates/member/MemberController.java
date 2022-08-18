package com.codestates.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {
    @GetMapping("/register")
    public String registerMemberForm() {
        return "member-register";
    }

    @PostMapping("/register")
    public String registerMember() {
        System.out.println("Member Login Successfully");
        return "login";
    }
}
