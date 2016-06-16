package com.dt.spark.design_pattern.Observer.Observe

/**
  * Created by peng.wang on 2016/6/9.
  */
trait Observer {
    def update( subject : Subject )
}

class ObserverA extends Observer {
    /**
      * myState需要跟目标对象的state值保持一致
      */
    var myState = 0
    override def update(subject: Subject): Unit = {
        myState = subject.asInstanceOf[ ConcreteSubject ].getState
        println( "观察者状态发生了改变！！！" )
    }
}
