package com.dt.spark.design_pattern.Bridge

/**
  * Created by 王鹏 on 2016/5/31.
  */
trait Brand {
    def sale() : Unit
}

class Lenovo extends Brand {
    override def sale(): Unit = {
        println( "Lenovo sale..." )
    }
}


class Dell extends Brand {
    override def sale(): Unit = {
        println( "Dell sale..." )
    }
}