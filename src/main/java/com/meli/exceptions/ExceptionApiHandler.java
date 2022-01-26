package com.meli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionApiHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MutantNotValidException.class)
    public ResponseEntity<?> mutantNotValidException(MutantNotValidException mutantNotValidException) {
        return new ResponseEntity<>(mutantNotValidException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = DnaAlreadyExistsException.class)
    public ResponseEntity<?> dnaAlreadyExistsException(DnaAlreadyExistsException dnaAlreadyExistsException) {
        return new ResponseEntity<>(dnaAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
    }

}
