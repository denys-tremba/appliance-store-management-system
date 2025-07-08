package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderSessionHandler;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class JwtCookieOrderHandlerInterceptor implements HandlerInterceptor {

    public static final String ORDER_COOKIE = "jwt-order";
    private final JwtService jwtService;
    private final ApplicationContext applicationContext;
    private final CompleteOrderService completeOrderService;
    private final JwtCookieFactory jwtCookieFactory;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        List<Cookie> cookies = Arrays.asList(request.getCookies());

        Optional<Cookie> orderHolder = jwtCookieFactory.findForAccess(cookies);

        if (orderHolder.isEmpty()) {
            CompleteOrderSessionHandler sessionHandler = new CompleteOrderSessionHandler();
            sessionHandler.setCompleteOrderService(completeOrderService);
            request.setAttribute("sessionHandler", sessionHandler);
            return true;
        }

        Cookie cookie = orderHolder.get();


        String compact = cookie.getValue();

        CompleteOrderSessionHandler sessionHandler = jwtService.sessionHandler(compact);
        sessionHandler.setCompleteOrderService(completeOrderService);

        request.setAttribute("sessionHandler", sessionHandler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        CompleteOrderSessionHandler sessionHandler = (CompleteOrderSessionHandler) request.getAttribute("sessionHandler");
        String compactForm = jwtService.createToken(sessionHandler);
        Cookie cookie = jwtCookieFactory.createForOrder(compactForm);
        response.addCookie(cookie);
    }
}
