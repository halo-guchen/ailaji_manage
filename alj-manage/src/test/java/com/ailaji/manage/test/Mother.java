package com.ailaji.manage.test;

public class Mother {
    
    
    public Double weight;
    
    public String address;
    
    public void clean(){
        System.out.println("mother clean the house!");
    }
    
    
    public class Son extends Persons{
        private String school;
        
        public Son(){
            super();
            weight=50.5;
            address="上海市";
            school="大学";
            clean();
        }
    }
    
    public static void main(String[] args) {
        Mother mother = new Mother();
        Son son = mother.new Son();
        son.print();
        System.out.println(mother.address);
    }

}
