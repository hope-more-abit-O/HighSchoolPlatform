package com.demo.admissionportal.exception;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Dữ liệu đã tồn tại", ex.getErrors());
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
        return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lưu thông tin thất bại.", ex.getErrors());
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {

        // Handle the exception here, e.g., return a custom error response.
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(ResponseData.error("Maximum upload size exceeded. Please upload a smaller file."));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        String targetField = "mark";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            String fullFieldName = ife.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .reduce("", (prev, curr) -> prev.isEmpty() ? curr : prev + "." + curr);
            // Extract the relevant part of the field name
            String fieldName = fullFieldName.contains(".") ? fullFieldName.substring(fullFieldName.lastIndexOf('.') + 1) : fullFieldName;
            if (targetField.equals(fieldName)) {
                errors.put(targetField, "Điểm không đúng định dạng");
            } else {
                String errorMessage = ife.getOriginalMessage();
                errors.put(fieldName, errorMessage);
            }
        } else if (cause instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) cause;
            String fullFieldName = jme.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .reduce("", (prev, curr) -> prev.isEmpty() ? curr : prev + "." + curr);
            String fieldName = fullFieldName.contains(".") ? fullFieldName.substring(fullFieldName.lastIndexOf('.') + 1) : fullFieldName;
            if (targetField.equals(fieldName)) {
                errors.put(targetField, "Điểm không đúng định dạng");
            } else {
                String errorMessage = jme.getOriginalMessage();
                errors.put(fieldName, errorMessage);
            }
        } else {
            errors.put("error", "Định dạng không hợp lệ.");
        }
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Sai format", errors);
    }
}
