package com.ailaji.manage.test;

public class ExceptionTest extends Exception {

    public ExceptionTest(String message) {
        super(message);
    }

    public ExceptionTest() {

    }

    public ExceptionTest(String msg, ExceptionTest e) {
        super(msg,e);
    }

}
