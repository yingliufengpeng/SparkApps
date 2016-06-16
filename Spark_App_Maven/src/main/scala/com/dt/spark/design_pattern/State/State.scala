package com.dt.spark.design_pattern.State

/**
  * Created by peng.wang on 2016/6/8.
  */
trait State {
    def handle : Unit
}

class FreeState extends State {
    override def handle: Unit = println( "房间空闲，没人住！！！" )
}

class BookedState extends State {
    override def handle: Unit = println( "房间以预定，别人不能住！！！" )
}

class CheckedState extends State {
    override def handle: Unit = println( "房间已经有人住，请勿打扰！！！" )
}