package com.simpleIoC.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simpleIoC.annotation.myAutowired;
import com.simpleIoC.annotation.myBean;

import java.util.List;

public class myBeanFactory {
    private final Map<String, Object> beanMap = new HashMap<>();
    private final List<String> ClassList = new ArrayList<>();

    private void scanClass(final String scanPackage) {
        final URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        System.out.println("\033[36murl:\033[36;4m" + url+"\033[0m");
        final String fileStr = url.getFile();
        final File file = new File(fileStr);

        final String[] filesStr = file.list();
        for (final String path : filesStr) {
            final File filePath = new File(fileStr + '/' + path);
            if (filePath.isDirectory()) {
                System.out.println("\033[33mThis is a folder: \033[33;4m" + filePath.getPath()+"\033[0m"+"\n");
                scanClass(scanPackage + '.' + path);// 递归到下一级
            } else {
                String classPath = scanPackage + '.' + filePath.getName();
                System.out.println("\033[32mThis is a file: \033[32;4m" + classPath+"\033[0m");
                if (classPath.endsWith(".class"))// 确保加入list的为.class文件
                    ClassList.add(classPath);// 全类路径名 尾部.class
            }
        }
    }

    public void doInstance(String beanPackage) {
        scanClass(beanPackage);
        System.out.println("\033[1;94m"+"-------------------------------doInstance---------------------------"+"\033[m");
        for (final String className : ClassList) {
            final String cn = className.replace(".class", "");
            System.out.println("\033[31m"+cn+"\033[m");
            try {
                final Class<?> clazz = Class.forName(cn);
                // 这里处理@myBean注解
                if (clazz.isAnnotationPresent(myBean.class)) {
                    String beanName = clazz.getSimpleName();
                    System.out.println("\033[31m    This is a bean -->\033[0m\033[33m beanName: \033[33;4m" + beanName + "\033[0m");
                    myBean mb = clazz.getAnnotation(myBean.class);//用于获取注解中的值
                    if(mb.id().equals("")){//若不设置bean的id，默认为my+类名
                        beanName = "my" + beanName;
                    }
                    else{//若设置了bean的id，直接使用
                        beanName = mb.id();
                    }
                    beanMap.put(beanName, clazz.newInstance());
                }
                else{
                    System.out.println();
                }
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void doAssemble(String Field_Autowired_Package) {
        scanClass(Field_Autowired_Package);
        System.out.println("\033[1;94m"+"-------------------------------doAssemble---------------------------"+"\033[m");
        for (final String className : ClassList) {
            final String cn = className.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                // 这里处理@myAutowired注解
                for (Field field : clazz.getDeclaredFields()) {
                    System.out.println("\033[34m"+cn + " : " +"\033[34;4m"+field.getName() + "\033[0m");
                    if (field.isAnnotationPresent(myAutowired.class)) {
                        String valueName = field.getType().getSimpleName();
                        System.out.println("    " +"\033[31;4m"+ field.getName()+"\033[m"+"\033[31m is a field need a "+"\033[31;4m"+valueName+"\033[m"+"\033[31m instance to Autowired"+"\033[0m");
                        myAutowired ma = field.getAnnotation(myAutowired.class);
                        if(ma.id().equals("")){
                            valueName = "my"+valueName;
                        }
                        else{
                            valueName = ma.id();
                        }
                        field.setAccessible(true);
                        Object value = beanMap.get(valueName);
                        if(ma.required()&&value==null)
                            throw new Exception("注入对象失败，无此id的bean！");
                        field.set(field, beanMap.get(valueName));
                    }
                }
            } catch (final ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (final Exception other){
                other.printStackTrace();
            }
        }
    }

    public myBeanFactory(final String beanPackage,final String Field_Autowired_Package) {
        Init(beanPackage,Field_Autowired_Package);
    }
    //无参构造，从默认包开始
    public myBeanFactory(){
        Init("com","com");//默认扫描com
    }

    private void Init(String beanPackage,String Field_Autowired_Package){
        doInstance(beanPackage);
        doAssemble(Field_Autowired_Package);
    }
    public void test() {
        for (Map.Entry<String, Object> bean : beanMap.entrySet()) {
            System.out.println(
                    "Get a Bean! The bean's key is:" + bean.getKey() + "||beans' value is————:" + bean.getValue());
        }
    }
}