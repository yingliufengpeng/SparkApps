package com.dt.spark.design_pattern.Builder

/**
  * Created by 王鹏 on 2016/5/31.
  */
object Client {
    def main(args: Array[String]) {
        val director = new SaxAirShipDirector( new SxtAirShipBuilder() )
        director.directAirShip
    }
}
