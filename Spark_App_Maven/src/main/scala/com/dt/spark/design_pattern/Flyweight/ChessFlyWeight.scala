package com.dt.spark.design_pattern.Flyweight

/**
  * 享元类
  * Created by peng.wang on 2016/6/2.
  */
trait ChessFlyWeight {
    def color : String
    def setColer( color : String ) : Unit
    def getColor() : String
    def display( c : Coordinate  )
}

class ConcreteChess(  var color : String ) extends ChessFlyWeight{

    override def setColer(color: String): Unit = ???

    override def display(c: Coordinate): Unit = {
        println( "棋子的颜色：" + color )
        println( "棋子的位置：" + c.getX + "___" + c.getY )
    }

    override def getColor(): String = color
}