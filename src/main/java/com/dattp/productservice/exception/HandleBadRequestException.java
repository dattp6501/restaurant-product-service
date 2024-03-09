package com.dattp.productservice.exception;


import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.EmptyFileException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dattp.productservice.dto.ResponseDTO;

/**
 * HandleBadRequestException
 */
@RestControllerAdvice
public class HandleBadRequestException {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO hanđleBindException(BindException e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getAllErrors().get(0).getDefaultMessage(),null
        );
    }

    @ExceptionHandler(com.fasterxml.jackson.core.JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO hanđleJsonParseException(Exception e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerBadRequestException(BadRequestException e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerSQLException(SQLException e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerIOException(IOException e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerEmptyFileException(EmptyFileException e){
        return new ResponseDTO(
            HttpStatus.BAD_REQUEST.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDTO handlerAccessDeniedException(AccessDeniedException e){
        return new ResponseDTO(
            HttpStatus.UNAUTHORIZED.value(), e.getMessage(),null
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO handlerException(Exception e){
        return new ResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null
        );
    }
}