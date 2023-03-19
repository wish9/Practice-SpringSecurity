package com.codestates.filter;

import javax.servlet.*;
import java.io.IOException;

public class FirstFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException { // 생성한 Filter에 대한 초기화 작업을 진행하는 메서드

    }

    public void doFilter(ServletRequest request, // 해당 Filter가 처리하는 실질적인 로직을 구현하는 메서드
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        // 다음 필터로 넘어가기 전에 전처리 작업 수행

        chain.doFilter(request, response); // 다음 필터 작업 수행

        // 후처리 작업 수행
    }

    public void destroy() { // Filter가 컨테이너에서 종료될 때 호출되는 메서드
        // 주로 Filter가 사용한 자원을 반납하는 처리 등의 로직을 작성
    }
}