package com.dt.spark.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by peng.wang on 2016/4/13.
  */
object DataFrameOps {
    def main(args: Array[String]) {
        val conf = new SparkConf()
        conf.setAppName( "Wow, My First Spark App!" )
//        conf.setMaster( "spark://Master:7077" )
        conf.setMaster( "local[4]" )
        val sc = new SparkContext( conf )
        val sqlContext = new SQLContext( sc )

        val df = sqlContext.read.json( "hdfs://Master:9000/user/data/people.json"  )

        df.show
        df.printSchema
        df.select( "name" ).show
        df.select( df( "name" ), df( "age" ) + 10 ).show
        df.groupBy( "age" ).count.show

        sc.stop
    }
}
