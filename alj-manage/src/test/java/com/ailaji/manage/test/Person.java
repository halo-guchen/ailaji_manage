package com.ailaji.manage.test;

public class Person implements Cloneable {

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

    public void person() {

    }

    public void person(String name) {
        this.name=name;
    }
    
    public void person(String name,Integer age){
        this.name=name;
        this.age=age;
    }
    
    protected Person clone() {
        Person person = null;
        try {
            person = (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return person;
    }

}
