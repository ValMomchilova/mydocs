package com.val.mydocs.web.interseptors;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class CustomizeTitleInterceptor extends HandlerInterceptorAdapter {

//    @Override
//    public void postHandle(HttpServletRequest request,
//                           HttpServletResponse response,
//                           Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        LocalDateTime now = LocalDateTime.now();
//        if (isUserLogged()) {
//            String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//            System.out.println(loggedUsername);
//        }
//    }


}
