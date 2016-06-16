package com.dt.spark.design_pattern.Singleton.Enum

/**
  * Created by peng.wang on 2016/6/9.
  */
object Enum extends Enumeration{
    type Enum = Value
    val LAUNCHING, LOADING, RUNNING, KILLED, FAILED, LOST, EXITED = Value
    def isFinished( state:Enum ):Boolean = {
        Seq(KILLED, FAILED, LOST, EXITED).contains( state )
    }
}
