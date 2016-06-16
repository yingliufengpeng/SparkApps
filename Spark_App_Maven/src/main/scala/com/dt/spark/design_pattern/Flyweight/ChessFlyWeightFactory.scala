package com.dt.spark.design_pattern.Flyweight

import scala.collection.mutable

/**
  * Created by peng.wang on 2016/6/2.
  */
object ChessFlyWeightFactory {
    val map = mutable.HashMap[ String , ChessFlyWeight ]()
    def getChess( color : String ) : ChessFlyWeight = {
        if( map.contains( color ) ){
            map( color )
        }else {
            val cfw = new ConcreteChess( color )
            map( color ) = cfw
            cfw
        }
    }
}
