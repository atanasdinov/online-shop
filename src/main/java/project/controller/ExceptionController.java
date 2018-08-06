package project.controller;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController{
    @ExceptionHandler(AccessDeniedException.class)
    public String unauthorized(){
        return "error-403";
    }

}