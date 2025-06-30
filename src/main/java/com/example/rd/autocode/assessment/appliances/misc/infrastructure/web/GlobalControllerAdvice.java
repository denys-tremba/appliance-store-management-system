package com.example.rd.autocode.assessment.appliances.misc.infrastructure.web;

import com.example.rd.autocode.assessment.appliances.order.complete.OrderException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ModelAndView onException(HttpServletRequest request, Exception e, RedirectAttributes redirectAttributes) {
        log.error("No way to handle this 💀 {}", e.getLocalizedMessage());
        ModelAndView modelAndView = createModelAndView(request);
        redirectAttributes.addFlashAttribute("warning", "Some error happened on the server. Please retry your actions.");
        return modelAndView;
    }

    private ModelAndView createModelAndView(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:" + referer);
        return modelAndView;
    }

    @ExceptionHandler(OrderException.class)
    public ModelAndView onDomainException(HttpServletRequest request, Exception e, RedirectAttributes redirectAttributes) {
        log.warn("😬Order completion did not go as planned: {}", e.getLocalizedMessage());
        ModelAndView modelAndView = createModelAndView(request);
        redirectAttributes.addFlashAttribute("warning", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ModelAndView onIntegrityViolationException(HttpServletRequest request, ObjectOptimisticLockingFailureException e, RedirectAttributes redirectAttributes) {
        log.warn("Somebody just outrun you while editing {}, id={} 🤬 {}", e.getPersistentClassName(),e.getIdentifier(), e.getLocalizedMessage());
        ModelAndView modelAndView = createModelAndView(request);
        redirectAttributes.addFlashAttribute("error", "Someone just edited the same %s as you did (id = %s). Your changes were discarded. Please reload the page and redo your edit.".formatted(e.getPersistentClass().getSimpleName(), e.getIdentifier()));
        return modelAndView;
    }

    @ModelAttribute("locales")
    List<Locale> locales() {
        return List.of(Locale.US, Locale.forLanguageTag("uk-UA"));
    }
}
