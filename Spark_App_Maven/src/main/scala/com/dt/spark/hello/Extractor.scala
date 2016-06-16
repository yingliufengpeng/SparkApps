package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object Extractor {
    def main(args: Array[String]) {
        val pattern = "([0-9]+) ([a-z]+)".r
        "2906631584 hadoop" match {
            case pattern( num , item ) => println( num + " : " + item )
        }
    }
}
