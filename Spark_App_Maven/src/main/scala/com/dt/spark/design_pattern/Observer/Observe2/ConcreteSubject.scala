package com.dt.spark.design_pattern.Observer.Observe2

import java.util.{Observable, Observer}

/**
  * Created by peng.wang on 2016/6/9.
  */
class ConcreteSubject extends Observable{
    var state = 0
    def getState = state
    def set( state : Int ) = {
        this.state = state

        /**
          * 表示目标做了更改
          */
        setChanged()

        /**
          * 通知所有的观察者
          */
        notifyObservers()


    }
}
