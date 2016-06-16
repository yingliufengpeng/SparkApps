package com.dt.spark.design_pattern.Proxy.DynamicProxy
import java.lang.reflect.Proxy
/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        val realStar = new RealStar
        val starHandler = new StarHandler( realStar )
        val proxy = Proxy.newProxyInstance( ClassLoader.getSystemClassLoader ,
                Array( classOf[ Star ] ) , starHandler ).asInstanceOf[ Star ]

//        proxy.bookTcket
        proxy.sing

    }
}
