package com.bjsx.test

/**
  * Created by peng.wang on 2016/6/6.
  */
object ClassLoader {
    def main(args: Array[String]) {
        val classLoader = Thread.currentThread().getContextClassLoader
        val demo05ClassLoader = Demo05.getClass.getClassLoader
        Thread.currentThread().setContextClassLoader( new FileSystemClassLoader )
        println( demo05ClassLoader )
        println( classLoader )

        println( Thread.currentThread().getContextClassLoader )
    }
}
