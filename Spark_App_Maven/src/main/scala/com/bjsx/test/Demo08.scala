package com.bjsx.test

/**
  * 自定义的FileSystemClassLoader
  * Created by peng.wang on 2016/6/4.
  */
object Demo08 {
    def main(args: Array[String]) {
        /**
          * 两个不同的加载器的实例，加载出的class对象也是不同的
          */
        val loader =  new FileSystemClassLoader
        val loader2 =  new FileSystemClassLoader
        val c = loader.loadClass( "com.bjsxt.peng.Hello" )
        val c2 = loader2.loadClass( "com.bjsxt.peng.Hello" )

        c.newInstance()
    }
}
