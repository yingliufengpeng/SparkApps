package com.dt.spark.design_pattern.Adapter

/**
  * Created by peng.wang on 2016/6/1.
  */
trait Target {
    def handleRequest : Unit
}

/**
  * 适配器本身
  * （相当于usb和ps/2的转接器）
  */

class Adpater( adapte : Adaptee) extends Target {
    override def handleRequest: Unit = adapte.request
}

//class Adapter extends Adaptee with Target
//{
//    override def handleRequest: Unit = super.request
//}
