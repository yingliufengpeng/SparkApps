package com.dt.spark.design_pattern.Strategy

/**
  * 负责和具体的策略类交互
  * 这样的话，具体的算法和直接的客户端调用分离了，
  * 是的算法独立于客户端独立的变化
  * Created by peng.wang on 2016/6/8.
  */
class Context( private var stategy : Strategy ){
    def setStrategy( strategy : Strategy ): Unit = this.stategy = stategy
    def printPrice( s : Double ) = println( "你的报价为：" + stategy.getPrice( s ) )

}
