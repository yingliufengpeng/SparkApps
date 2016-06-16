package com.dt.spark.design_pattern.Command

/**
  * Created by peng.wang on 2016/5/31.
  */
trait Command {
    /**
      * 这个方法是一个返回结果为空的方法，
      * 实际项目中，可根据需求来设计不同的方法
      */
    def exectute() : Unit
}


class ConcreteCommand( val receiver : Receiver ) extends Command {
    /**
      * 这个方法是一个返回结果为空的方法，
      * 实际项目中，可根据需求来设计不同的方法
      */
    override def exectute(): Unit = {

    /**
      * 命令真正执行前或后，执行相关的处理！！！
      */
        receiver.action()
    }
}