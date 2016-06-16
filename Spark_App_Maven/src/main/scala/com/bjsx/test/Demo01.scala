package com.bjsx.test

import java.util

/**
  * 测试java.lang.Class对象的获取方式
  * Created by peng.wang on 2016/6/3.
  */
object Demo01 {
    def main(args: Array[String]) {
        val path = "com.bjsx.test.bean.User"
        try{
            /**
              * 对象是表示或封装一些数据，一个类被加载后，JVM会创建一个对于该类的Class对象，
              * 类的整个结构的信息会放到对应的Class对象中。这个Class对象就像一面镜子一样，通过
              * 这面镜子我们可以看到对一个类的全部信息
              */
            val clazz = Class.forName( path )   //体现了一个类只对应一个反射对象
            val clazz2 = Class.forName( path )
            println( clazz.hashCode() )
            println( clazz2.hashCode() )

            val strClasszz = classOf[ String ]
            val intClasszz = classOf[ Int ]
            println( strClasszz )
            println( intClasszz )

            val arr = Array( 2 , 3 ,Array( 2 , 3, 4 ) )
            println( arr.getClass )


        }catch{
            case e : Exception => e.printStackTrace()
        }
    }
}
