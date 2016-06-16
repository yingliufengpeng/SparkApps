package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/18.
  */
object TupleOps {
    def main(args: Array[String]) {
        val triple = ( 100 , "Scala" , "Spark" )
        println( triple._1 )
        println( triple._2 )
        println( triple._3 )
    }
}
