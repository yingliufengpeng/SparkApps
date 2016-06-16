package com.bjsx.test

import java.util

/**
  * Created by peng.wang on 2016/6/4.
  */
object Demo05 {
    def main(args: Array[String]) {

    }
}

class B{
    println( "创建B类的对象" )
}

trait A_Father{
    println( "静态初始化A_Father" )
}

object A extends B{
    final val width = 100
    val arr = new util.ArrayList( 45 )
    println( "静态初始化A" )
    println( "高兴的设置每个不同的应用类型！！！" )
}
