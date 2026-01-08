package com.leaderboard.lead.Config;

import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitIncptor implements HandlerInterceptor{

    private final BucketPerUserCache bucketPerUserCache;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) throws Exception {
        // TODO Auto-generated method stub
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        log.info("dOING Some thing");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
        Cookie cookies[] = request.getCookies() != null ? request.getCookies() : null;
        String userId= "";
        if(cookies == null) return true;
        for(Cookie c : cookies) {
            if(c.getName().equals("X-USER-ID")) {
                userId = c.getValue();
            }
            
        }
        if(request.getRequestURI().contains("/score")) {
             if(!bucketPerUserCache.isAllowed(userId)) {
                response.setStatus(429);
                response.getWriter().write("TOO MANY REQUESTS");
                return false;
             }
        }
        return true;
    }

}
