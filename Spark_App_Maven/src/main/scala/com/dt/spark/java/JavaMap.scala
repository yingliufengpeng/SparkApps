package com.dt.spark.java



/**
  * Created by peng.wang on 2016/4/21.
  */

import scala.collection.mutable.Map
object JavaMap {
    def main(args: Array[String]) {
        val data = Map[ String , Int ]()
        data( "Hadoop" ) = 10
        data( "Spark" ) = 6
        data( "Kafka" ) = 4

        val m = data.contains( "Spark" )

//


    }
}
