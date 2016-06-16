package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
trait 银行 {
    def openAccount: Unit
}

class 中国工商银行 extends 银行{
    override def openAccount: Unit = println( "在中国银行开户！！！" )
}