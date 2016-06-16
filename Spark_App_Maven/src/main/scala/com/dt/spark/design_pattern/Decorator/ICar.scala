package com.dt.spark.design_pattern.Decorator

/**
  * 抽象组件
  * Created by peng.wang on 2016/6/1.
  */
trait ICar {
    def move : Unit
}

/**
  * 真实的对象，被装饰的对象
  */
class Car extends ICar {
    override def move: Unit = println( "陆地上跑！！！" )
}

/**
  * 装饰器的角色
  * @param car
  */
abstract class SuperCar( val car : ICar ) extends ICar {
    override def move: Unit = car.move
}

class FlyCar(  car : ICar ) extends SuperCar( car ) {
    def fly = println( "天上飞！" )

    override def move = {
        super.move
        fly
    }
}

class WaterCar(  car : ICar ) extends SuperCar( car ) {
    def swim = println( "水上游！" )

    override def move = {
        super.move
        swim
    }
}


class AICar(  car : ICar ) extends SuperCar( car ) {
    def autoMove = println( "自动跑！" )

    override def move = {
        super.move
        autoMove
    }
}
//class A( val age : Int ){
//    def Age = age
////    def setage( newAge : Int ) = { age = newAge  ; age }
//}
//
//class B( override val age : Int , mm : Int ) extends A( age ){
//
//    def BBage = age
//    def MM = mm
//}

