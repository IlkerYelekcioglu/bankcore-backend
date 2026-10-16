package com.bankcore.bankcore_backend.common.exception;

public class DuplicateCustomerException extends RuntimeException {

  public DuplicateCustomerException(String message) {
    super(message);
  }
}
