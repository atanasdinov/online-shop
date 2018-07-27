package project.controller;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import project.exception.EntityNotFoundException;

@ControllerAdvice
public class ExceptionController{
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView getException(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("exceptions/notFound");
        return modelAndView;
    }
    @ExceptionHandler(AccessDeniedException.class)
    public String unauthorized(){
        return "error-403";
    }

}