package com.dt.spark.hello

/**
  * Created by peng.wang on 2016/5/19.
  */
object Option_Internal {
    def main(args: Array[String]) {
        val scores = Map( "Alice" -> 99 , "Spark" -> 100 )

        scores.get( "Alice" ) match {
            case Some( score ) => println( score )
            case None => println( "No score" )
        }
    }
}
