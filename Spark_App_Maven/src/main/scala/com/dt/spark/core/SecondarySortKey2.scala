package com.dt.spark.core

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by peng.wang on 2016/3/3.
  */
object SecondarySortKey2
{
    def main(args: Array[String]) {
        val conf = new SparkConf()
        conf.setAppName("RDDBasedOnCollections")
        conf.setMaster("local")

        val sc = new SparkContext( conf )
        val lines = sc.textFile( "D:\\scala\\WordCount2\\words.txt" )
        val pairWithSortKey = lines.map
            {
                line =>
                    {
                        val splited = line.split( " " )
                        ( new SecondarySortKey( splited( 0 ).toInt , splited( 1 ).toInt ) , line )
                    }
            }
        val sorted = pairWithSortKey.sortByKey( false )
        val sortedResult = sorted.map( sortedLine => sortedLine._2 )
        pairWithSortKey.collect.foreach( println )
        sortedResult.collect.foreach( println )

        sc.stop
    }
}
