package com.dt.spark.design_pattern.Adapter

/**
  * Created by peng.wang on 2016/6/1.
  * 被适配的类
  * （相当于例子中的键盘，PS/2键盘）
  */

class Adaptee {
    def request = println( "可以完成客户请求需要的功能！！！" )
}
