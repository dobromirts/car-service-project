package com.tsvetkov.autoservice.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FaviconInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url="https://res.cloudinary.com/dzxceo64w/image/upload/v1570088649/favicon_njvnuh.ico";
        if (modelAndView!=null){
            modelAndView.addObject("favicon",url);
        }
    }
}
