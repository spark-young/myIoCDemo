package com.entity;

import com.simpleIoC.annotation.myBean;

@myBean//不设置bean的id，默认为my+类名
public class Student {
    private String name;
    private int age;
    public Student(){
        this.name = "spark";
        this.age = 21;
    }
    public Student(String name){
        this.name = name;
        this.age = 21;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }
    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "name:"+name+"|age: "+age;
    }
}