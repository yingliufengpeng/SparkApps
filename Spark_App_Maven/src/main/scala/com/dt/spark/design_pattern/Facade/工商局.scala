package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
trait 工商局 {
    def checkName : Unit
}

class 海淀工商局 extends 工商局{
    override def checkName: Unit = println( "检查名字是否有冲突！！！" )
}