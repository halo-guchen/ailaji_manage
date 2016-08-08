package com.ailaji.manage.test;

public class Persons {
    
    private String name;
    
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    protected void print() {
        System.out.println("persons printing!");
    }

}
