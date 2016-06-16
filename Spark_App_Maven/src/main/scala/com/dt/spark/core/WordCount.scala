package com.dt.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
   def main(args: Array[String]): Unit = {
         System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
         val conf = new SparkConf()
         conf.setAppName( "Wow, My First Spark App!" )
         conf.setMaster( "local" )

         val sc = new SparkContext( conf )

         val lines = sc.textFile( "resources/people.txt" )

         val words = lines.flatMap{ line => line.split( " " ) }
         val pairs = words.map{ word => ( word , 1 ) }
         val wordCounts = pairs.reduceByKey( _ + _ )
           //     val wordCounts = pairs.sortByKey( false )
         wordCounts.collect.foreach( w_pair =>  println( w_pair._1 + " : " + w_pair._2 ) )
    //     org.apache.spark.repl.Main
         while( true )
         sc.stop
     
     
  }
}



