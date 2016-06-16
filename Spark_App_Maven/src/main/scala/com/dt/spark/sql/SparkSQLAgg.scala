package com.dt.spark.sql

import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types._
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.functions._

/**
  * Created by peng.wang on 2016/4/15.
  * 是用Spark SQL中的内置函数对数据进行数据分析，Spark SQL API不同的是，DataFrame中的内置函数操作的结果
  * 是返回一个Column对象，而DataFrame天生就是“A distributed collection of data organized into named colums”，
  * 这就为数据的复杂分析建立了坚实的基础，并提供了极大的方便性，例如说，我们在操作DataFrame的方法中可以随时
  *调用内置函数进行业务需要的处理，这之于我们构建复杂的业务逻辑而言是可以极大的减少不必要的时间消耗，（基本上
  * 就是实际模型的映射），让我们聚焦在数据分析上，这对了于提高工程师的生产力而言是非常有价值的。
  * Spark1.5x开始提供了大量的内置函数，例如agg
  *  def agg(aggExpr: (String, String), aggExprs: (String, String)*): DataFrame = {
  * groupBy().agg(aggExpr, aggExprs : _*)
  * }
  * 还有max、mean、min、sum、avg、explode、size、sort、sort_array、to_date、abs、acros、asin、atan
  * 总体上而言内置函数包含了五大基本类型：
  * 1，聚合函数，例如countDistinct 、sumDistinct等
  * 2，集合函数，例如sort_array、explode等
  * 3，时期、日期函数、例如hour、quarter、next_day
  * 4，数学函数：例如asin、atan、sqrt、tan、round等
  * 5，开窗函数，例如rowNumber等
  * 6，字符串函数，concat、format_number、rexexp_extract等
  * 7，其他函数，isNaN、sha、randn、callUDF
  */
object SparkSQLAgg {
    def main(args: Array[String]) {

        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        val conf = new SparkConf()
        conf.setAppName( "Wow, My First Spark App!" )
        conf.setMaster( "local" )

        val sc = new SparkContext( conf )

        //要使用Spark SQL的内置函数，就一定要导入SQLContext的隐式转换


        val sqlContext = new SQLContext( sc )  //构建SQL上下文
        import  sqlContext.implicits._


        //第三步：模拟电商访问的数据，实际情况下的数据会比模拟数据复杂的多
        val userData = Array(
            "2016-3-27,001,http://spark.apache.org/,10",
            "2016-3-27,001,http://hadoop.apache.org/,11",
            "2016-3-27,002,http://fink.apache.org/,12",
            "2016-3-28,003,http://kafka.apache.org/,10",
            "2016-3-28,004,http://spark.apache.org/,11",
            "2016-3-28,002,http://hive.apache.org/,12",
            "2016-3-28,001,http://parquet.apache.org/,15",
            "2016-3-28,001,http://spark.apache.org/,18"
        )


        val userDataRDD = sc.parallelize( userData ) //生产RDD分布式集合对象

        /*
            根据业务需要对数据进行预处理生成DataFrame，要想把RDD转换成DataFrame，需要先把RDD 中的元素类型
            变成Row类型与此同时，要提供DataFrame中的Columns的元数据信息描述

         */

        val userDataRDDRow = userDataRDD.map( row =>
            {
                val splited = row.split( ",")
                Row( splited( 0 ) , splited( 1 ).toInt , splited( 2 ) , splited( 3 ).toInt  )
            }
        )

        val structTypes = StructType (
            Array(
                StructField( "time" , StringType , true ) ,
                StructField( "id" , IntegerType , true ) ,
                StructField( "url" , StringType , true ) ,
                StructField( "amount" , IntegerType , true )
            )

        )


        val userDataDF = sqlContext.createDataFrame(userDataRDDRow,structTypes)





        /*
            第五步：使用Spark1 SQL提供的内置函数对DataFRame进行操作，特别注意：内置函数生成的Column对象且自动进行CG代码生成

         */

//        userDataDF.groupBy( "time" ).agg( 'time , countDistinct( 'id ) )
//            .map( row => Row( row( 1 ) , row( 2 ) ) ).collect.foreach( println )

        userDataDF.groupBy( "time" ).agg( 'time , sum( 'amount) ).show

    }
}
