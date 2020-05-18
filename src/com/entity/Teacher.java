package com.entity;

import com.simpleIoC.annotation.myBean;

@myBean(id="myteacher")//设置了bean的id，获取bean时需使用该id
public class Teacher {
    private String name;
    private String teach;
    public Teacher(){
        this.name = "Jackson";
        this.teach = "JavaEE";
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the teach
     */
    public String getTeach() {
        return teach;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param teach the teach to set
     */
    public void setTeach(String teach) {
        this.teach = teach;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "name:"+name+"|teach:"+teach;
    }
}