package com.demo.admissionportal.exception;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.exception.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getErrors());
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
        return new ResponseData<>(HttpStatus.NOT_FOUND.value(), (ex.getMessage() == null) ? "Dữ liệu không tồn tại": ex.getMessage(), ex.getErrors());
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
        return new ResponseData<>(HttpStatus.NOT_FOUND.value(), (ex.getMessage() == null) ? "Không có quyền thực hiện.": ex.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(QueryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object> handleQueryExceptionException(QueryException ex) {
        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex.getErrors());
        }
        return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Có lỗi khi query tại database.", ex.getMessage());
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
    public ResponseData<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String message = ex.getMessage();
        logger.error("HttpMessageNotReadableException: {}", message);
        Map<String, String> errors = new HashMap<>();
        if (message.contains("Unexpected character")) {
            message = "Định dạng không đúng.";
            errors.put("error", message);
        } else if (message.contains("Cannot deserialize value of type `java.lang.Float`")) {
            message = "Định dạng không đúng.";
            errors.put("error", message);
        } else if (message.contains("Unrecognized field")){
            message = "Request chứa thông tin không hợp lệ";
            errors.put("error", message);
        } else if(message.contains("JSON parse error: Chỉ cho phép số tự nhiên")){
            message = "Chỉ cho phép số tự nhiên";
            errors.put("error", message);
        } else if (message.contains("JSON parse error: Môn học không tồn tại")){
            message = "Tổ hợp môn học có chứa môn học không tồn tại";
            errors.put("error", message);
        }
        return new ResponseData<>(ResponseCode.C205.getCode(), "Sai format", errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
        String name = ex.getParameterName();
        String message = String.format("Request param %s không tìm thấy", name);
        return new ResponseEntity<>(new ResponseData<>(ResponseCode.C205.getCode(), message), HttpStatus.BAD_REQUEST);
    }
}
