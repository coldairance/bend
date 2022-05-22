package com.blog.security.exception;

public class AccountLoginByOtherException extends RuntimeException {
    public AccountLoginByOtherException() {
        super();
    }

    public AccountLoginByOtherException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
