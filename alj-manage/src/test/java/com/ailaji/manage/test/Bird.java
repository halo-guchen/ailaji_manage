package com.ailaji.manage.test;

import java.util.ArrayList;

public class Bird {
    public void fly() throws ExceptionTest {
        throw new ExceptionTest("test,系统有错误!");
    }

    public static void main(String[] args) throws ExceptionTest {
        try {
            Bird bird = new Bird();
            bird.fly();
        } catch (ExceptionTest e) {
           throw new ExceptionTest("错误了",e);

        }
    }

}
