package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring Security Configuration 적용 = 인증 방식, 웹 페이지에 대한 접근 권한 설정 가능
public class SecurityConfiguration {

    // HttpSecurity클래스 = HTTP 요청에 대한 보안 설정을 구성하기 위한 핵심 클래스
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 권한부여
        http
                .headers().frameOptions().sameOrigin() // 웹 브라우저에서 H2 웹 콘솔을 정상적으로 사용하기 위한 설정
                .and()
                .csrf().disable()                 // CSRF 공격에 대한 Spring Security에 대한 설정을 비활성화 (로컬 환경에서 테스트하기 때문)
                .formLogin()                      // 기본적인 인증 방법을 폼 로그인 방식으로 지정
                .loginPage("/auths/login-form")   // 만들어 둔 커스텀 로그인 페이지를 사용하도록 설정
                .loginProcessingUrl("/process_login")    // 로그인 인증 요청을 수행할 요청 URL을 지정
                .failureUrl("/auths/login-form?error")   // 로그인 실패시 뜨게 할 화면 지정
                .and()
                .logout()                        // 로그아웃 설정을 위한 LogoutConfigurer 를 리턴
                .logoutUrl("/logout")            // 로그아웃을 수행하기 위한 request URL을 지정 ("/" = 홈, 메인화면)
                .logoutSuccessUrl("/")  // 로그아웃 이후 리다이렉트 할 URL 지정
                .and() // Spring Security 보안 설정을 메서드 체인 형태로 구성
                .exceptionHandling().accessDeniedPage("/auths/access-denied")   // 권한이 없는 사용자가 특정 request URI에 접근할 경우 발생하는 에러를 처리하기 위한 페이지 설정
                .and()
                .authorizeHttpRequests(authorize -> authorize                  //  request URI에 대한 접근 권한을 부여
                        .antMatchers("/orders/**").hasRole("ADMIN")        // ADMIN을 부여받은 사용자만 /orders로 시작하는 모든 URL에 접근할 수 있게 설정 (order 하위 URL 모두 접근 가능, *한개만 쓰면 /Order의 하위 URL depth가 1인 URL만 허용
                        .antMatchers("/members/my-page").hasRole("USER")   // USER Role을 부여받은 사용자만 /members/my-page URL에 접근할 수 있음
                        .antMatchers("⁄**").permitAll()                    // 앞에서 지정한 URL 이외의 나머지 모든 URL은 Role에 상관없이 접근이 가능
                );

        return http.build();
    }

//    @Bean  InMemory설정 삭제
//    public UserDetailsManager userDetailsService() { // 정보 추가
//
//        UserDetails userDetails = // user 정보를 포함하는 인터페이스
//                User.withDefaultPasswordEncoder()    // 디폴트 패스워드 인코더를 이용해 사용자 패스워드를 암호화
//                        .username("wjwee9@gmail.com") // username 설정
//                        .password("1111")            // 비번 설정
//                        .roles("USER")               // 역할을 USER로 설정
//                        .build();
//
//        UserDetails admin = // 관리자 권한을 갖는 사용자 추가
//                User.withDefaultPasswordEncoder()
//                        .username("admin@gmail.com")
//                        .password("2222")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(userDetails, admin); // 위에서 설정한 userDetails을 구현해서 UserDetailsManager객체로 만들어 리턴
//    }

    @Bean // PasswordEncoder를 Bean으로 등록
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();  // DelegatingPasswordEncoder가 실질적으로 PasswordEncoder 구현 객체를 생성해 준다.
    }

}
