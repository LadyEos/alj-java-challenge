package jp.co.axa.apidemo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FailedValidationException extends RuntimeException {

  @Getter
  private final String errorMessage;

  @Getter
  private final String path;

}
