package edu.search.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SearchExceptionHandler extends ResponseEntityExceptionHandler {

    public SearchExceptionHandler(){
        int i =1;
    }

    @ExceptionHandler(value = InvalidSearchModeException.class)
    protected ResponseEntity<ApiError> handleInvalidSearchModeException(InvalidSearchModeException e) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Invalid Request" , e.getMessage());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);

    }
}
