# 实现IoC Container

持续更新myIoCDemo项目

## day1:

今天着手实现IoC Container，力求以最小规模先行实现IoC，之后在此基础上做补充

#### 任务：注解方式实现@myBean和@myAutowired

@myBean(id="bean_id")：只注解field，id为bean实例的id，缺省值为“my”+类名

@myAutowired(required="true",id="bean_id")：只注解type，required为该属性是否必须，默认为true表示必须装配，装配失败会报“注入对象失败，无此id的bean！”异常，设为false则装配失败也不会报出异常；id为bean实例的id，缺省值同@myBean

将IoC模块放在com.simpleIoC包下，此包中含有annotation子包和reflection子包

annotation：注解的定义

reflection：反射实现依赖注入

#### reflection——myBeanFactory（DI实现）

实例化的bean使用Map存储，key为String类型（存放id），value为Object类型（存放实例）

这是一个bean工厂类，bean的创建和管理都在这个类中，使用时需要在main方法中实例化该类

Constructor：公有无参构造和公有带参构造（参数为final **String** beanPackage, final **String** Field_Autowired_Package，表示扫描bean的包和扫描有Autowired注解属性的类所在的包)

主要方法：scanClass（加载类）、doInstance（创建被myBean修饰的类的实例）、doAssemble（装配被myAutowired修饰的属性）

#### scanClass(final **String** scanPackage)：

从scanPackage开始加载其下所有class，通过获取scanPackage对应的Resource URL来定位该包对应系统中的路径，然后扫描该路径下的所有文件和文件夹：如果是文件且结尾是“.class”就加入ClassList（一个存储Class名的List），如果是文件夹就递归调用scanClass继续扫描。完成后scanPackage下的所有class的全类名都被加载进ClassList

#### doInstance(**String** beanPackage):

从beanPackage开始加载其下所有被@myBean修饰的类，并实例化该类以规定或缺省的bean_id存储到Map中

首先执行一次scanClass，用于将beanPackage中的类的信息加载进来。而后开始从ClassList中扫描被@myBean注解的类，使用isAnnotationPresent(myBean.class)判断：若是则该类被@myBean修饰，需要将其实例化并存储Map中，该entry的key缺省为my+clazz.getSimleName()，或为myBean实例通过id()获取；若否则跳过

#### **doAssemble**(**String** Field_Autowired_Package)：

从Field_Autowired_Package开始扫描其下所有被@myAtutowired修饰的属性，按照该属性的类型和myAutowired设定的id从map中获取bean实例并注入给该属性

首先执行一次scanClass，用于将Field_Autowired_Package下所有类加载进来。而后开始从ClassList中扫描被@myAutowired注解的属性，属性由对应className的实例clazz.getDeclaredField获取，通过isAnnotationPresent(myAutowired.class)判断该属性field是否被@myAutowired修饰，若是则根据@myAutowired中id从map中获取bean，该id缺省时为my+field.getType().getSimleName()，或为myAutowired实例通过id()获取，以此id作为key从map中获取bean后，先通过myAutowired实例required()查看是否设置了required为true：若为true则当从map中获取bean==null时抛出异常"注入对象失败，无此id的bean！"，若为false则即使bean==null也可以通过，最后通过field.set(field，beanMap.get(id))将bean装配给属性；若否则跳过

#### test():

书写test是将所有bean打印出来，方便查看

