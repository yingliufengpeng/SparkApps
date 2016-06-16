package com.dt.spark.hello

import scala.io.Source

/**
  * Created by peng.wang on 2016/5/18.
  */
object LazyOps {
    def main(args: Array[String]) {
        lazy val file = Source.fromFile( "resources/mm.txt" )

    }
}
