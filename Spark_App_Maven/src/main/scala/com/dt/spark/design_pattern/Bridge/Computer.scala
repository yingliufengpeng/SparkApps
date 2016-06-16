package com.dt.spark.design_pattern.Bridge

/**
  * Created by 王鹏 on 2016/5/31.
  */
//class Computer2( brand: Brand ) {
//    def sale() = brand.sale()
//}

abstract class Computer {
    val brand : Brand
    def sale() : Unit
}

class Desktop( val brand: Brand) extends Computer {

    override def sale: Unit = {
        brand.sale()
    }
}

class Ipad( val brand: Brand) extends Computer {

    override def sale: Unit = {
        brand.sale()
    }
}

class LapTop( val brand: Brand) extends Computer {

    override def sale: Unit = {
        brand.sale()
    }
}