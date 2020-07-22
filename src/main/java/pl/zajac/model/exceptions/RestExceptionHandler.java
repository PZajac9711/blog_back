package pl.zajac.model.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.zajac.model.exceptions.custom.InvalidUserData;
import pl.zajac.model.exceptions.custom.UserRegistrationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "There's no request body";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception,error));
    }
    @ExceptionHandler(UserRegistrationException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserRegistrationException ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex,"There's problem with creating user");
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidUserData.class)
    protected ResponseEntity<Object> handleInvalidUserData(InvalidUserData ex){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,ex,"There's problem with data from user");
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }
}
