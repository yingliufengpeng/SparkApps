package com.dt.spark.design_pattern.Builder

/**
  * Created by 王鹏 on 2016/5/31.
  */
class AirShip {
    private[this] var _orbitalModule: OrbitalModule = _
    private[this] var _engine: Engine = _
    private[this] var _escapeTower: EscapeTower = _

    def this( orbitalModule : OrbitalModule , engine: Engine , escapeTower: EscapeTower) = {
        this
        _orbitalModule = orbitalModule
        _engine = engine
        _escapeTower = escapeTower
    }





}

class OrbitalModule {
    private[this] var _name: String = _

    def name: String = _name

    def name_=(value: String): Unit = {
      _name = value
    }

}


class Engine {
    private[this] var _engine: String = _

    private[this] def engine: String = _engine

    private[this] def engine_=(value: String): Unit = {
      _engine = value
    }
}

class EscapeTower {
    private[this] var _escapeTower: String = _

    private[this] def escapeTower: String = _escapeTower

    private[this] def escapeTower_=(value: String): Unit = {
      _escapeTower = value
    }
}