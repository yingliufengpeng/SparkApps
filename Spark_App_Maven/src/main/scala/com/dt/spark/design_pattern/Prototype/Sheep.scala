package com.dt.spark.design_pattern.Prototype

import java.util.Date

/**
  * Created by peng.wang on 2016/6/1.
  */
class Sheep ( var name : String , var birthday : Date ) extends Cloneable with Serializable{   //1997,英国的克隆羊，多莉！
    /**
      * 直接调用了object对象的clone（）的方法！
      * @return
      */
    def getname = name
    def getbirthday = birthday
    override def clone(): AnyRef = {
        val s = super.clone( ).asInstanceOf[ Sheep ]
        s.name = name
        s.birthday = new Date()
        s
    }
}
