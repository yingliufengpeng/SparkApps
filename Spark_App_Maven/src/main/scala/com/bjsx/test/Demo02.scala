package com.bjsx.test

/**
  * 应用发射机制的API，获取类的信息（类的名字、属性、方法、构造器等）
  * Created by peng.wang on 2016/6/3.
  */
object Demo02 {
    def main(args: Array[String]) {
        val path = "com.bjsx.test.bean.User"

        /**
          * 对象是表示或封装一些数据，一个类被加载后，JVM会创建一个对于该类的Class对象，
          * 类的整个结构的信息会放到对应的Class对象中。这个Class对象就像一面镜子一样，通过
          * 这面镜子我们可以看到对一个类的全部信息
          */
        val clazz = Class.forName( path )   //体现了一个类只对应一个反射对象

//        /**
//          * 获得类的名字
//          */
//        println( clazz.getName )    //获得包名加类名
//        println( clazz.getSimpleName )  //获得类名
//
//        /**
//          * 获得类相关的属性、方法和构造器
//          */
//
//        println( clazz.getDeclaredFields.size )
//        clazz.getDeclaredFields.foreach( println )
//
//        /**
//          * 获取Method的信息
//          */
//
//        clazz.getDeclaredMethods.foreach( println )
//        val method1 = clazz.getDeclaredMethod( "name"  )

        /**
          * 获得构造器信息
          */
        val constructors = clazz.getDeclaredConstructors
        constructors.foreach( println )
    }
}
