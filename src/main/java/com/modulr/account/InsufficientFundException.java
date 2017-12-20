package com.modulr.account;

class InsufficientFundException extends RuntimeException
{
    InsufficientFundException(String message) {
        super(message);
    }
}
