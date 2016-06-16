package com.dt.spark.design_pattern.Decorator

/**
  * Created by peng.wang on 2016/6/1.
  */
object Client {
    def main(args: Array[String]) {
//        val car = new Car()
//        car.move
//        println( "增加新的功能，飞行----------" )
//        val flyCar = new FlyCar( car )
//        flyCar.move
//
//        println( "增加新的功能，水里游----------" )
//        val waterCar = new WaterCar( car )
//        waterCar.move

        println( "增加新的功能，水里游、天上飞----------" )
        val waterCar2 = new WaterCar( new FlyCar( new Car ) )
        waterCar2.move

    }
}
