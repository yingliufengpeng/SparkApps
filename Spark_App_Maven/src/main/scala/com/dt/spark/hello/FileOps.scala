package com.dt.spark.hello

import scala.io.Source

/**
  * Created by peng.wang on 2016/5/18.
  */
object FileOps {
    def main(args: Array[String]) {
        val file = Source.fromFile( "resources/people.txt" )
        for( line <- file.getLines() ) {
            println( line )
        }
    }
}
