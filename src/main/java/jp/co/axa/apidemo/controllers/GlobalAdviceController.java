package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.ErrorResponse;
import jp.co.axa.apidemo.exceptions.BadRequestException;
import jp.co.axa.apidemo.exceptions.FailedValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdviceController {

  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @org.springframework.web.bind.annotation.ExceptionHandler(FailedValidationException.class)
  public ResponseEntity<ErrorResponse> handleFailedValidationException(FailedValidationException e) {
    ErrorResponse errorResponse =  new ErrorResponse("error/validation-error","failed field validation",
        "412",e.getErrorMessage(),e.getPath());
    return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
    ErrorResponse errorResponse =  new ErrorResponse("error/bad-request-error","bad request",
        "400",e.getErrorMessage()," ");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

}
