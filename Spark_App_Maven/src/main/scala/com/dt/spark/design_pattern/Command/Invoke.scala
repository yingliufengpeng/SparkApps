package com.dt.spark.design_pattern.Command

/**
  * Created by peng.wang on 2016/5/31.
  */


/**
  *
  * @param command 也可以通过容器List<Command>容纳很多命令对象，进行批处理。
  *                数据库底层的事务管理就是类似的结构
  *
  */
class Invoke( val command : Command ) {
    /**
      * 业务方法，用于调用命令类的方法
      */
    def call() : Unit = {
        command.exectute()
    }
}
