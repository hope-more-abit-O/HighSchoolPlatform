package com.demo.admissionportal.exception;

import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handle validation exceptions response data.
     *
     * @param ex the ex
     * @return the response data
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errors.getOrDefault(fieldName, "") + errorMessage + " ");
        });
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Sai format", errors);
    }

    /**
     * Handle validation exceptions response data.
     *
     * @param ex the ex
     * @return the response data
     */
    @ExceptionHandler(DataExistedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<Object> handleDataExistedExceptions(DataExistedException ex) {
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Dữ liệu đã tồn tại", ex.getMessage());
    }

    /**
     * Handle validation exceptions response data.
     *
     * @param ex the ex
     * @return the response data
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseData<Object> handleResourceNotFountExceptions(ResourceNotFoundException ex) {
        return new ResponseData<>(HttpStatus.NOT_FOUND.value(), "Dữ liệu không tồn tại.", ex.getMessage());
    }

    /**
     * Handle validation exceptions response data.
     *
     * @param ex the ex
     * @return the response data
     */
    @ExceptionHandler(StoreDataFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object> handleStoreDataFailedException(StoreDataFailedException ex) {
        return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lưu thông tin thất bại.", ex.getMessage());
    }

    /**
     * Handle validation exceptions response data.
     *
     * @param ex the ex
     * @return the response data
     */
    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseData<Object> handleNotAllowedException(NotAllowedException ex) {
        return new ResponseData<>(HttpStatus.UNAUTHORIZED.value(), "Không có quyền thực hiện.", ex.getMessage());
    }
}
