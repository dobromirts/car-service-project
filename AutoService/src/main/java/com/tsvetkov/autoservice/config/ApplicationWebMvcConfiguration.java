package com.tsvetkov.autoservice.config;

import com.tsvetkov.autoservice.web.interceptors.FaviconInterceptor;
import com.tsvetkov.autoservice.web.interceptors.PageTitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {
    private final PageTitleInterceptor pageTitleInterceptor;
    private final FaviconInterceptor faviconInterceptor;

    @Autowired
    public ApplicationWebMvcConfiguration(PageTitleInterceptor pageTitleInterceptor, FaviconInterceptor faviconInterceptor) {
        this.pageTitleInterceptor = pageTitleInterceptor;
        this.faviconInterceptor = faviconInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageTitleInterceptor);
        registry.addInterceptor(faviconInterceptor);
    }
}
