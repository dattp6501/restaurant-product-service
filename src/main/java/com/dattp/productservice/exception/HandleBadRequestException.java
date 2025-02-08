package com.dattp.productservice.exception;


import com.dattp.productservice.base.ErrorMessage;
import com.dattp.productservice.base.response.BaseResponse;
import org.apache.poi.EmptyFileException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

/**
 * HandleBadRequestException
 */
@RestControllerAdvice
public class HandleBadRequestException {
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handleBindException(BindException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getAllErrors().get(0).getDefaultMessage())
        .build();
  }

  @ExceptionHandler(com.fasterxml.jackson.core.JsonParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handleJsonParseException(Exception e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handlerBadRequestException(BadRequestException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(SQLException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handlerSQLException(SQLException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handlerIOException(IOException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(EmptyFileException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handlerEmptyFileException(EmptyFileException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.BAD_REQUEST.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseResponse handlerAccessDeniedException(AccessDeniedException e) {
    return BaseResponse.builder()
        .code(ErrorMessage.UNAUTHORIZED.getStatus().value())
        .message(e.getMessage())
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse handlerException(Exception e) {
    return BaseResponse.builder()
        .code(ErrorMessage.INTERNAL_SERVER_ERROR.getStatus().value())
        .message(e.getMessage())
        .build();
  }
}