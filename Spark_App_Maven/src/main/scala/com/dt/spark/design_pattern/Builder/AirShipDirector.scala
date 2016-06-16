package com.dt.spark.design_pattern.Builder

/**
  * Created by 王鹏 on 2016/5/31.
  */
trait AirShipDirector {
    /**
      * 组装飞船对象
      * @return
      */
    def directAirShip : AirShip
}

class SaxAirShipDirector( builder : AirShipBuilder ) extends AirShipDirector {
    /**
      * 组装飞船对象
      *
      * @return
      */
    override def directAirShip: AirShip = {
        new AirShip( builder.builderOrbitalModule , builder.builderEngine , builder.builderEscapeTower  )
    }
}