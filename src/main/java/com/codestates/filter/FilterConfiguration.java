package com.codestates.filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration { // FirstFilter를 적용하기 위한 FilterConfiguration 구성

    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilterRegister()  {
        FilterRegistrationBean<FirstFilter> registrationBean = new FilterRegistrationBean<>(new FirstFilter());
        registrationBean.setOrder(1); // FirstFilter가 첫번째로 동작하도록 순서 지정
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SecondFilter> secondFilterRegister()  {
        FilterRegistrationBean<SecondFilter> registrationBean = new FilterRegistrationBean<>(new SecondFilter());
        registrationBean.setOrder(2); // SecondFilter가 두번째로 동작하도록 순서 지정
        return registrationBean;
    }
}
