package com.bjsx.test

import com.bjsx.test.bean.User

/**
  * 通过反射API、获取构造器、方法、字段
  * Created by peng.wang on 2016/6/3.
  */
object Demo03 {
    def main(args: Array[String]) {
        val path = "com.bjsx. test.bean.User"
        try{
            /**
              * 对象是表示或封装一些数据，一个类被加载后，JVM会创建一个对于该类的Class对象，
              * 类的整个结构的信息会放到对应的Class对象中。这个Class对象就像一面镜子一样，通过
              * 这面镜子我们可以看到对一个类的全部信息
              */
            val clazz = Class.forName( path )   //体现了一个类只对应一个反射对象
            val obj = clazz.newInstance().asInstanceOf[ User ]

            //通过反射操作方法
            val method = clazz.getDeclaredMethod( "print"  )
            method.invoke( obj )

            //通过反射操作属性
            clazz.getDeclaredFields.foreach( println )
        }catch{
            case e : Exception => e.printStackTrace()
        }
    }
}
