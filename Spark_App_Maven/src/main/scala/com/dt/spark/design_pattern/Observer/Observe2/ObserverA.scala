package com.dt.spark.design_pattern.Observer.Observe2

import java.util.{Observable, Observer}

/**
  * Created by peng.wang on 2016/6/9.
  */
class ObserverA extends Observer {
    var myState = 0
    override def update(o: Observable, arg: scala.Any): Unit = {
        myState = o.asInstanceOf[ ConcreteSubject ].getState
        println( "观察者状态发生了变化！！！" )
    }
}
