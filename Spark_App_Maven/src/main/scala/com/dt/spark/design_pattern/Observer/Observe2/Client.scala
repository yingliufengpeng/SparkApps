package com.dt.spark.design_pattern.Observer.Observe2

/**
  * Created by peng.wang on 2016/6/9.
  */
object Client {
    def main(args: Array[String]) {
        /**
          * 创建目标对象
          */
        val subject = new ConcreteSubject

        /**
          * 创建多个观察者
          */
        val obs1 = new ObserverA
        val obs2 = new ObserverA
        val obs3 = new ObserverA
        val obs4 = new ObserverA

        /**
          * 将四个观察者添加到subject对象的观察者队伍中
          */
        subject.addObserver( obs1 )
        subject.addObserver( obs2 )
        subject.addObserver( obs3 )
        subject.addObserver( obs4 )

        /**
          * 改变subject的状态
          */
        subject.set( 30 )
        subject.set( 20 )
    }
}
