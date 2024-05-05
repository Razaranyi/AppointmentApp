package EasyAppointment.appointmentscheduler.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a global exception handler for the application.
 * It catches exceptions that are thrown during the execution of the application and returns appropriate HTTP responses.
 * It is annotated with @ControllerAdvice, which makes it applicable to all @Controller classes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method builds an error response to be returned when an exception is caught.
     * It takes a message and a status as parameters, and returns a ResponseEntity with these details.
     * @param message The error message.
     * @param status The HTTP status.
     * @return A ResponseEntity with the error details.
     */
    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        String refinedMessage = extractMessage(message);

        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", refinedMessage);
        return new ResponseEntity<>(body, status);
    }

    /**
     * This method extracts the error message from the exception message.
     * If the message contains "=", it extracts the substring after "=" and before "}" or the end of the message.
     * @param message The exception message.
     * @return The extracted error message.
     */
    private String extractMessage(String message) {
        if (message.contains("=")) {
            int index = message.indexOf('=') + 1;
            int endIndex = message.indexOf('}', index);
            if (endIndex == -1) {
                endIndex = message.length();
            }
            return message.substring(index, endIndex).trim();
        }
        return message;
    }

    // The following methods are exception handlers for different types of exceptions.
    // They are annotated with @ExceptionHandler, which makes them handle exceptions of the specified type.
    // When an exception of the specified type is thrown, the corresponding method is invoked to handle it.
    // Each method returns a ResponseEntity with the error details by calling the buildErrorResponse method.

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoClassDefFoundError.class)
    public ResponseEntity<Object> handleNoClassDefFoundError(NoClassDefFoundError ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyOwnException.class)
    public ResponseEntity<Object> handleUserAlreadyOwnsBusiness(UserAlreadyOwnException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return buildErrorResponse(errors.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String detailedMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        Pattern pattern = Pattern.compile("null value in column \"([^\"]*)\"");
        Matcher matcher = pattern.matcher(detailedMessage);

        String errorMessage = "Database error: An unexpected error occurred.";
        if (matcher.find()) {
            String columnName = matcher.group(1);
            errorMessage = String.format("Invalid input: The '%s' cannot be null.", columnName);
        }
        return buildErrorResponse(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return buildErrorResponse("Resource not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<Object> handleFavoriteAlreadyExistsException(FavoriteAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppointmentAlreadyBookedException.class)
    public ResponseEntity<Object> handleAppointmentAlreadyBooked(AppointmentAlreadyBookedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        return buildErrorResponse("Token has expired, please log-in", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorResponse("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex) {
        return buildErrorResponse("Optimistic locking failure: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

}