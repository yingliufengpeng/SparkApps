package com.dt.spark.jvm.basics

/**
  * Created by peng.wang on 2016/4/25.
  * 从JVM调用的角度分析Java程序对内存空间上的使用：
  * 当JVM进程启动的时候，会从类加载路径中找到包含main
  * 方法的入口类（HelloJVM），找到后会读取文件中的二进制
  * 数据并且把该类的信息放到运行时的Method的内存区域中，
  * 然后会定位到HelloJVM的main方法的字节码中并开始执行main
  * 方法中的指令
  * 此时会创建Student实例对象并且使用student来应用该对象（或者
  * 说该对象命名），其内幕如下：
  *     第一步：JVM会直接到Method区域中去查找Studen类的信息，
  *     此时如果没有发现Student类，就通过类加载器加载该Student文件
  *
  *     第二步：在JVM的Method区域加载并找到了Student类之后会在Heap
  *     区域中为Student实例对象分配内存并且在Student的实例对象中持有
  *     指向方法区域中Student类的引用（内存地址）
  *
  *     第三步：JVM实例化完成后，会在当前线程中为Stackre中的reference
  *     建立实际的应用关系，此时会复制给student，
  *
  *     第四步：在JVM中方法的调用一定是属于线程的行为，也就是方法调用
  *     本身发生在调用线程的方法调用栈中，线程的方法调用栈（Method Stack
  *     Frames ）,每一个方法的调用就是方法调用栈中的一个Frame，该Frame包含
  *     了方法的参数、局部变量、临时数据等
  *         student.sayHello
  */
object HelloJVM {
    /*
        在JVM在运行的时候会通过反射的方式到Method区域找到入口类方法main
     */

    def main(args: Array[String]) { //main这个方法是放在方法区域中的
        /*
            student是放在主线程中Stack区域中的；
            而Student对象的实例是放在所有线程共享的Heap的区域中的
         */
        val student = new Stutdent( "dt_Spark" )

        /*
            首先会通过student指针（句柄）找Student对象，当找到该对象后会通过
            内部指向方法区域中的指针来调用具体的方法去执行任务
         */
//        ThreadLocal
        student.sayHello
    }
}

class Stutdent
{
    self =>
    private var name : String = _  //name本身做为成员是放在Stack区域中的，但是name指向的String对象是Heap中

    def this(  name : String )
    {
        this()
        self.name = name

    }

    def sayHello: Unit =   //sayHello这个方法是放在方法区域中的
    {
        println( "Hello , this is " + this.name )
    }
}