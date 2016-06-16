package com.dt.spark.sql

import java.text.SimpleDateFormat
import java.util.{Date, Calendar}

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkContext, SparkConf}

/*
    在此之中所创建的数据表为
    create table userLogs( date String , tiemstamp bigint , userID bigint , pageID bigint , channel String , action String )
     row format delimited fields terminated by '\t' lines terminated by '\n'
 */

/**
  * Created by peng.wang on 2016/4/18.
  */
object SparkSQLUserlogsOps {

    def pvStatistic(hiveContext: HiveContext, currentDay: String): Unit = {
        hiveContext.sql(" use hive ")
        val sqlText = " SELECT date , pageID , pv " +
            " FROM " + "( SELECT date , pageID , count( * ) pv FROM userLogs " +
            " WHERE action = 'View' AND date = " + " '2016-04-17' " + " Group BY date , pageID ) subquery " +
            " ORDER BY pv DESC limit 10 "
        hiveContext.sql(sqlText).show()

        //把执行的结果放在数据库或者Hive中
    }

    def hotChannel(hiveContext: HiveContext, currentDay: String): Unit =
    {
        hiveContext.sql(" use hive ")
        val sqlText = " SELECT date , channel , channelpv " +
            " FROM " + "( SELECT date , channel , count( * ) channelpv FROM userLogs " +
            " WHERE action = 'View' AND date = " + " '2016-04-17' " + " Group BY  date , channel ) subquery " +
            " ORDER BY channelpv DESC"
        hiveContext.sql(sqlText).show()
    }

    def uvStatistic(hiveContext: HiveContext, currentDay: String) =
    {
        hiveContext.sql("use hive")
        val sqlText = " SELECT date , pageID , uv  " +
            " FROM ( SELECT date , pageID , count( 1 ) uv " +
                " FROM  ( SELECT date , pageID , userID  FROM userLogs " +
                " WHERE action = 'View' AND date = " + " '2016-04-17' " + " Group BY  date , pageID , userID ) " +
            " subquery GROUP BY date , pageID ) result " +
            " ORDER BY uv DESC "
        hiveContext.sql(sqlText).show()
    }

    def jumpOutStatistic(hiveContext: HiveContext, currentDay: String): Unit =
    {
        hiveContext.sql("use hive")
        val totalPvSQL = "SELECT count( 1 ) from userLogs where action = 'View' AND date = '2016-04-17' "
        val  pv2OneSQL = " SELECT count( 1 )  from ( SELECT count( 1 ) pvPerUser FROM userLogs where action = 'View' " +
            " AND date = '2016-04-17' Group BY userID HAVING  pvPerUser = 1 ) result  "
        val totalPv = hiveContext.sql(totalPvSQL).collect.head.get( 0 )
        val pv2One = hiveContext.sql(pv2OneSQL).collect.head.get( 0 )
        val rate = pv2One.toString.toDouble / totalPv.toString.toDouble

        println( )
        println( "跳出率: " + rate )
        println()
    }

    def newUserRegisterPercentStatistic(hiveContext: HiveContext, currentDay: String): Unit =
    {
        hiveContext.sql("use hive")
        val newUserSQL = "SELECT count( 1 ) from userLogs where action = 'View' AND date = '2016-04-17' " +
                " AND userID is NULL  "
        val  yesterdayREgistered =  "SELECT count( 1 ) from userLogs where action = 'View' AND date = '2016-04-17' "

        val totalPv = hiveContext.sql(yesterdayREgistered).collect.head.get( 0 )
        val pv2One = hiveContext.sql(newUserSQL).collect.head.get( 0 )
        val rate = pv2One.toString.toDouble / totalPv.toString.toDouble

        println( )
        println( "模拟新用户注册比例：　" + rate )
        println()
    }

    def main(args: Array[String]) {
//        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )

        val conf = new SparkConf().setMaster( "spark://Master:7077" ).setAppName( "SparkSQLUserlogsOps" )
        val sc = new SparkContext( conf )
        val hiveContext = new HiveContext( sc )

        val currentDay = getCurrentDay  //因为数据在2016-04-17已经产生，这个只是作为一个基本假定了！！！
//        pvStatistic( hiveContext , currentDay )  //pv
//        uvStatistic( hiveContext , currentDay )  //uv
//        hotChannel( hiveContext , currentDay )   //热门板块
        jumpOutStatistic( hiveContext , currentDay ) //用户跳出率
        newUserRegisterPercentStatistic( hiveContext , currentDay ) //新用户注册的比例


    }

    def getCurrentDay: String =
    {
        val date = new SimpleDateFormat( "yyyy-MM-dd" )
        val cal = Calendar.getInstance
        cal.setTime( new Date )
        cal.add( Calendar.DATE , -2 )
        val currentDay = cal.getTime


        date.format( currentDay )
    }
}
