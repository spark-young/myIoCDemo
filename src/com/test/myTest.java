package com.test;

import com.simpleIoC.annotation.myAutowired;
import com.entity.Student;
import com.entity.Teacher;
import com.simpleIoC.reflection.myBeanFactory;

public class myTest {
	
	@myAutowired//由于该类型对应的bean默认id，此处也为默认
	public static Student stu;

	//@myAutowired(required = false, id="my")//将required设为false，即使无法加载bean也可以通过
	@myAutowired(id="myteacher")//默认required为true
	//@myAutowired(id="my")//默认required为true，此时无法加载bean会抛出异常
	public static Teacher teacher;
	public static void main(final String[] args) throws Exception {
		//指定bean在com.entity下扫描，有依赖的类在com.test下扫描
		myBeanFactory beans = new myBeanFactory("com.entity","com.test");
		//默认bean在com下扫描，有依赖的类在com下扫描
		//myBeanFactory beans = new myBeanFactory();

		beans.test();//输出所有bean的实例，用于测试
		
		System.out.println("\033[1;94m"+"-------------------------------Main---------------------------"+"\033[m");
		System.out.println(stu);//myAutowired自动装配的Student类实例
		System.out.println(teacher);//myAutowired自动装配的Teacher类实例
	}
}