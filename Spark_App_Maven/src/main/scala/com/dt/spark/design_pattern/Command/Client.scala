package com.dt.spark.design_pattern.Command

/**
  * Created by peng.wang on 2016/5/31.
  */
object Client {
    def main(args: Array[String]) {
        val c : Command = new ConcreteCommand( new Receiver() )
        val i = new Invoke( c )
        i.call()
    }
}
