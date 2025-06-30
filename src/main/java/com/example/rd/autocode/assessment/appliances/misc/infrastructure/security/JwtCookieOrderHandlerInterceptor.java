package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.example.rd.autocode.assessment.appliances.order.Order;
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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        List<Cookie> cookies = Arrays.asList(request.getCookies());

        Optional<Cookie> orderHolder = cookies.stream().filter(c -> c.getName().equals(ORDER_COOKIE)).findAny();
        if (orderHolder.isEmpty()) {
            request.setAttribute("order", new Order());
            return true;
        }

        Cookie cookie = orderHolder.get();


        String compact = cookie.getValue();

        Order order = jwtService.order(compact);

        request.setAttribute("order", order);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Order order = (Order) request.getAttribute("order");
        String compactForm = jwtService.create(order);
        Cookie cookie = new Cookie(ORDER_COOKIE, compactForm);
        cookie.setPath("/orders/current");
        response.addCookie(cookie);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
