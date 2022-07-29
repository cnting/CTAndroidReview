package com.example.aspectjdemo;

/**
 * Created by cnting on 2022/7/29
 */
public class Person {
    private String name;
    private int age;

    public Person(){
        this.name = "aaa";
        this.age = 10;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
