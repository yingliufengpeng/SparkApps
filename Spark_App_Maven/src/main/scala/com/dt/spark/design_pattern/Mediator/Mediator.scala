package com.dt.spark.design_pattern.Mediator

import scala.collection.mutable

/**
  * Created by peng.wang on 2016/6/2.
  */
trait Mediator {
    def register( name : String , d : Department ) : Unit
    def command( name : String ) : Unit

}

class President extends  Mediator {
    var map = mutable.HashMap[ String , Department ]()
    override def register(name: String, d: Department): Unit = {
        map( name ) = d
    }

    override def command(name: String): Unit = map( name ).selfAction
}
