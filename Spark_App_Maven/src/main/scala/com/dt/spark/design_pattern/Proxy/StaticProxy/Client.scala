package com.dt.spark.design_pattern.Proxy.StaticProxy

/**
  * Created by peng.wang on 2016/6/8.
  */
object Client {
    def main(args: Array[String]) {
        val realStar = new RealStar
        val proxy = new ProxyStar( realStar )

        proxy.confer
        proxy.signContract
        proxy.bookTcket
        proxy.sing()
        proxy.collectMoney
    }
}
