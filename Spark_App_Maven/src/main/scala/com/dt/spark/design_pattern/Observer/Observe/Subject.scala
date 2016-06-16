package com.dt.spark.design_pattern.Observer.Observe

import scala.collection.mutable.ArrayBuffer

/**
  * Created by peng.wang on 2016/6/9.
  */
class Subject {
    private val list = ArrayBuffer[ Observer ]()
    def register( obs : Observer ) = list += obs
    def removeObserver( observer: Observer ) = list -= observer

    /**
      * 通知所有的观察者更新状态
      */
    def notifyAllObserver = {
        list.map( obs => obs.update( this ) )
    }

}

class ConcreteSubject extends Subject {
    var state = 0
    def getState = state
    def setState( state : Int ) = {
        this.state = state
        this.notifyAllObserver
    }
}
