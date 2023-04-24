package jp.co.axa.apidemo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BadRequestException extends RuntimeException {

  @Getter
  private final String errorMessage;

}
