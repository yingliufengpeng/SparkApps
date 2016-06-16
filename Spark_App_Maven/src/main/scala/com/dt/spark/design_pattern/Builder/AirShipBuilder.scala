package com.dt.spark.design_pattern.Builder

/**
  * Created by 王鹏 on 2016/5/31.
  */
trait AirShipBuilder {
    def builderEngine : Engine
    def builderEscapeTower : EscapeTower
    def builderOrbitalModule : OrbitalModule
}

class SxtAirShipBuilder extends AirShipBuilder{
    override def builderEngine: Engine = {
        println( "尚学堂发动机！！！" )
        new Engine()
    }

    override def builderOrbitalModule: OrbitalModule = {
        println( "尚学堂轨道舱！！！" )
        new OrbitalModule()
    }

    override def builderEscapeTower: EscapeTower = {
        println( "尚学堂逃生舱！！！" )
        new EscapeTower()
    }
}
