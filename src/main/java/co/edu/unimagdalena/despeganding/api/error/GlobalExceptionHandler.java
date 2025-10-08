package co.edu.unimagdalena.despeganding.api.error;

import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

//For handling all backend exceptions which are RuntimeExceptions and don't need try-catch
//When the program throws a RuntimeException, it will come here

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIError> handleNotFoundException(NotFoundException ex, WebRequest request) {
        var body = APIError.of(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        //Bring the vars that produces exceptions
        var violations = ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> new APIError.FieldViolation(fe.getField(), fe.getDefaultMessage())).toList();
        var body = APIError.of(HttpStatus.BAD_REQUEST, "Validation failed",
                request.getDescription(false), violations);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIError> handleConstraint(ConstraintViolationException ex, WebRequest request) {
        //Bring the vars that breaks the rules
        var violations = ex.getConstraintViolations().stream()
                .map(cv -> new APIError.FieldViolation(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
        var body = APIError.of(HttpStatus.BAD_REQUEST, "Constraint violation",
                request.getDescription(false), violations);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class) //When a method receives invalid argument
    public ResponseEntity<APIError> handleIllegalArgs(IllegalArgumentException ex, WebRequest request) {
        var body = APIError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), List.of());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIError> handleIllegalState(IllegalStateException ex, WebRequest req) {
        var body = APIError.of(HttpStatus.CONFLICT, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class) //For generic exceptions
    public ResponseEntity<APIError> handleGeneric(Exception ex, WebRequest req) {
        var body = APIError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error",
                req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
