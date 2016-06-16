package com.dt.spark.design_pattern.Adapter

/**
  * Created by peng.wang on 2016/6/1.
  * 客户端类
  * （相当于例子中的笔记本，只有USB接口）
  */
object Client {
    def main(args: Array[String]) {
        val adaptee = new Adaptee()
        val target = new Adpater( adaptee )
        test1( target )
    }

    def test1( t : Target ) = t.handleRequest
}
