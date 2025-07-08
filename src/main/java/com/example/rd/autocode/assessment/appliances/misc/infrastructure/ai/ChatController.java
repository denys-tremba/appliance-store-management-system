package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.security.JwtCookieFactory;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.security.JwtService;
import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderSessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders/current/chat")
@RequiredArgsConstructor
public class ChatController {
    private final AiAssistant aiAssistant;
    private final JwtService jwtService;
    private final JwtCookieFactory jwtCookieFactory;


    @GetMapping("/test")
    @ResponseBody
    public String promptForResponse(HttpServletRequest request,
                                    HttpServletResponse response) throws JOSEException, JsonProcessingException {
        Cookie cookie = new Cookie("shit", "poo");
        response.addCookie(cookie);
        return "hi";
    }

    @GetMapping("/ai")
    @ResponseBody
    public String promptForResponse(@RequestAttribute("sessionHandler") CompleteOrderSessionHandler sessionHandler,
                                    @RequestParam("prompt") String prompt,
                                    @AuthenticationPrincipal User user,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws JOSEException, JsonProcessingException {
        Map<String, Object> context = Map.of("sessionHandler", sessionHandler);
        String answer = aiAssistant.promptForResponse(prompt, user, context);
        String compactForm = jwtService.createToken(sessionHandler);
        Cookie cookie = jwtCookieFactory.createForOrder(compactForm);
        response.addCookie(cookie);
        return answer;
    }

    @GetMapping(value = "/memory")
    @ResponseBody
    public List<String> loadMemory(@AuthenticationPrincipal User user) {
        return aiAssistant.retrieveMemoryForUser(user);
    }

    @ExceptionHandler(NonTransientAiException.class)
    @ResponseBody
    public String onRateLimit(NonTransientAiException e) {
        return "Sorry, can you retry later";
    }
}

