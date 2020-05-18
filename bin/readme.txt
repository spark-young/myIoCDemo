此项目实现IoC的注解形式
@myBean(id = "bean_id")
@myAutowired(required = true,id = "bean_id")
对bean的默认id规定为：my+类名
IoC模块在com.simpleIoC下
需在main方法中实例化myBeanFactory,
可选指定包名下扫描bean和有autowired修饰属性的类所属的包，默认都为com包

myAutowired的required默认为true，此时若id或默认id对应的bean 无法加载，将抛出异常