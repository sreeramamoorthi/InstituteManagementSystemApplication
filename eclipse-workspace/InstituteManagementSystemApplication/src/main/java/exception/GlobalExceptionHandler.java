package exception;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 409 - Duplicate resource (e.g. email already exists)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 400 - Bean validation failures (@Valid on request body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed for one or more fields",
                request.getRequestURI(),
                validationErrors
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 400 - Wrong path variable / param type (e.g. /students/abc instead of /students/1)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("'%s' should be a valid %s",
                ex.getValue(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "value");

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 409 - Database constraint violations (e.g. unique constraint on email)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "A record with the same unique field (e.g. email) already exists",
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 500 - Catch-all fallback for anything unexpected
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred: " + ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



//401 - Bad credentials on login
@ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
public ResponseEntity<ErrorResponse> handleBadCredentials(
        org.springframework.security.authentication.BadCredentialsException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            false,
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            "Invalid username or password",
            request.getRequestURI(),
            null
    );

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
}

// 403 - Authenticated but not authorized
@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
public ResponseEntity<ErrorResponse> handleAccessDenied(
        org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            false,
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.getReasonPhrase(),
            "You do not have permission to access this resource",
            request.getRequestURI(),
            null
    );

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
}

}