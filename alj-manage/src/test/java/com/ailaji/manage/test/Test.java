package com.ailaji.manage.test;

import java.io.UnsupportedEncodingException;

public class Test {
    public static void main(String[] args) {
        String s = "你好啊";
        try {
            String s2 = new String(s.getBytes("GBK"), "GBK");
            System.out.println(s2);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
} 