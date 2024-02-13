package tse.api.demo.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tse.api.demo.exception.TradeNotFoundException;

@ControllerAdvice
public class TradeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(TradeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(TradeNotFoundException ex) {
        return ex.getMessage();
    }
}