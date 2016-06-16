package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
trait 税务局 {
    def taxCertificate : Unit
}


class 海淀税务局 extends 税务局{
    override def taxCertificate: Unit = println( "在海淀税务局办理税务登记！！！" )
}