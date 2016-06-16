package com.dt.spark.core

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.MapPartitionsRDD
/**
  * Created by peng.wang on 2016/3/2.
  */
object RDDBasedOnCollections_OnlocalFile {
    def main(args: Array[String]) {
        val conf = new SparkConf()
        conf.setAppName("RDDBasedOnCollections")
        conf.setMaster("local")

        val sc = new SparkContext(conf)

        val rdd = sc.textFile( "D:/Program Files (x86)/spark/README.md" )
        var m = true
        while( m )
            {
                val sum = rdd.map( line => line.length ).reduce( _ + _ )
                println("lengthes of file = " + sum)
                m = false

            }




  }
}
