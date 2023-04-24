package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {

  @Getter
  private final String type;

  @Getter
  private final String title;

  @Getter
  private final String status;

  @Getter
  private final String detail;

  @Getter
  private final String instance;

}
