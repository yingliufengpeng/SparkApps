package com.dt.spark.hello

import scala.collection.SortedMap

/**
  * Created by peng.wang on 2016/5/18.
  */
object Map_Tuple {
    def main(args: Array[String]) {
        val map = Map( "book" -> 10 , "gun" -> 18 , "ipad" -> 1000 )
        for( ( k , v ) <- map ) yield ( k , v * 0.9 )

        val scores = scala.collection.mutable.Map( "Scala" -> 7 , "Hadoop" -> 8 , "Spark" -> 10 )
        val hadoopScores = scores.getOrElse( "Hadoop" , 0 )
        scores += ( "R" -> 9 )
        scores -= "Hadoop"

        val sortedScored = scala.collection.immutable.SortedMap( "Scala" -> 7 , "Hadoop" -> 8 , "Spark" -> 10 )



    }
}
