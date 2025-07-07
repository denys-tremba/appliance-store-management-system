package com.example.rd.autocode.assessment.appliances.misc.infrastructure.ai;

import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chat")
@SessionAttributes("orderService")
@RequiredArgsConstructor
public class ChatController {
    private final AiAssistant aiAssistant;
    private final ApplicationContext context;


    @GetMapping(value = "/ai", params = "prompt")
    @ResponseBody
    public String promptForResponse(@ModelAttribute("orderService")CompleteOrderService completeOrderService, @RequestParam("prompt") String prompt, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        Map<String, Object> context = Map.of("orderService", completeOrderService, "sessionStatus", sessionStatus);
        return aiAssistant.promptForResponse(prompt, user, context);
    }

    @GetMapping(value = "/memory")
    @ResponseBody
    public List<String> loadMemory(@AuthenticationPrincipal User user) {
        return aiAssistant.retrieveMemoryForUser(user);
    }

    @ModelAttribute(name = "orderService")
    CompleteOrderService completeOrderService() {
        return context.getBean(CompleteOrderService.class);
    }

    @ExceptionHandler(NonTransientAiException.class)
    @ResponseBody
    public String onRateLimit(NonTransientAiException e) {
        return "Sorry, can you retry later";
    }
}

