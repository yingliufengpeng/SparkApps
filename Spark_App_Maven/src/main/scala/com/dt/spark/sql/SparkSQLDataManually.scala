package com.dt.spark.sql

import java.text.SimpleDateFormat

import org.apache.spark.sql.types.CalendarIntervalType

/**
  * Created by peng.wang on 2016/4/18.
  * 论坛数据自动生成，数据格式如下
  * date：日期，格式：yyyy-MM-dd
  * timestamp：时间戳
  * userID：用户ID
  * pageID：页面ID
  * chanelID：板块ID
  * action：点击和注册
  */
object SparkSQLDataManually {
    def main(args: Array[String]) {
        val userLogBuffer = new StringBuilder( "" )
        var numberItems = 5000L
        if( args.length > 0 )
        {
            numberItems = args( 0 ).toInt
        }
        println( "User log number is : " + numberItems )
        //具体的论坛频道
        val channelNames = Array( "Spark" , "Scala" , "Kafka" , "Flink" , "Hadoop" , "Storm" , "Hive" , "Impala" , "HBase"  , "ML" )
        /*
            昨天的时间生成
         */
        val date = new SimpleDateFormat( "yyyy-MM-dd" )


    }
}
