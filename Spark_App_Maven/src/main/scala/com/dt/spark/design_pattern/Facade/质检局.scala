package com.dt.spark.design_pattern.Facade

/**
  * Created by peng.wang on 2016/6/3.
  */
trait 质检局 {
    def orgCodeCertificate : Unit
}


class 海淀质检局 extends 质检局 {
    override def orgCodeCertificate: Unit = println( "在海淀质检局办理组织机构代码证！！！" )
}
