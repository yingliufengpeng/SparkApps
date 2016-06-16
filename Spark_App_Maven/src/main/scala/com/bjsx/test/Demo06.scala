package com.bjsx.test

/**
  * Created by peng.wang on 2016/6/4.
  */
object Demo06 {
    def main(args: Array[String]) {
        println( ClassLoader.getSystemClassLoader )
        println( ClassLoader.getSystemClassLoader.getParent )
        /**
          * 因为最顶层类的加载器是原生类是用c/c++编写的，所以我们
          * 获取不到
          */
        println( ClassLoader.getSystemClassLoader.getParent.getParent )

        println( System.getProperty( "java.class.path" ) )
    }
}
