package com.dt.spark.sql

/**
  * Created by peng.wang on 2016/4/15.
  */

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


/**
  * @author peng.wang
  *
  *
  *
  *         这是个提到到Master上的程序！！！
  */
object SparkSQL2Hive {

    def main(args: Array[String]): Unit = {

//        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        val conf = new SparkConf().setMaster( "spark://Master:7077" ).setAppName("SparkSQL2Hive")
        val sc = new SparkContext(conf)
//        val sqlContext = new SQLContext(sc)
        /*
            第一：在目前企业级大数据Spark开发的时候，绝大数情况下是采用Hive作为数据仓库
                Spark提供了Hive的支持功能，Spark通过HiveContext可以直接操作Hive中的数据
                基于HiveContext我们可以使用sql/hql两种方式来编写SQL语句对Hive进行操作，包括
                创建表、删除表、往表里导入数据以及用SQL语法构造各种SQL语法构造各种SQL语句对
                表中的数据进行CRUD（增删改查）操作
            第二：我们也可以直接公国saveAsTAble的方式把DataFrame中的数据保存到Hive数据仓库中
            第三：可以直接通过HIveContext.table方法来直接加载Hive中的表二生成DataFrame

         */
        val hiveContext = new HiveContext( sc )
        hiveContext.sql( "use hive" )  //使用Hive数据仓库中的hive数据库
        hiveContext.sql( "drop table if exists people")  //删除同名的table
        hiveContext.sql( "create table if not exists people( name string , age Int ) " +
            "row format delimited fields terminated by '\\t' lines terminated by '\\n'" )  //创建自定义的table

        /*
            把本地数据加载到hive数据仓库中（背后实际上发生了数据的copy）
            当然也可以通过LOAD DATA INPATH 去获得HDFS等上面的数据到Hive（此时发生了数据的移动）
         */
        hiveContext.sql( "LOAD DATA LOCAL INPATH 'D:\\scala\\peng\\Spark\\resource\\people.txt' INTO TABLE people" )

        hiveContext.sql( "drop table if exists peopleScores")
        hiveContext.sql( "create table if not exists peopleScores( name string , age Int )" +
            "row format delimited fields terminated by '\\t' lines terminated by '\\n'" )  //创建自定义的table
        hiveContext.sql( "LOAD DATA LOCAL INPATH 'D:\\scala\\peng\\Spark\\resource\\people.txt' INTO TABLE peopleScores" )


        /*
           通过HiveContext使用join直接基于Hive中的两种表进行操作获得大于90分的人的name、age、score
         */

        val resultDF = hiveContext.sql( "SELECT pi.name , pi.age , ps.score "
                                    +
            "FROM people  pi JOIN peopleScores  ps ON pi.name = ps.name where ps.score > 90" )

        /*
            通过savaAsTable创建一张Hive Mananged Table，数据的元数据和数据即将放的具体位置是
            由Hive数据仓库进行管理的，当删除该表的时候，数据也要回一起被删除（磁盘上的数据不在存在）
         */
        hiveContext.sql( "drop table if exists peopleInfomationResult" )
        resultDF.saveAsTable( "peopleInfomationResult" )

        /*
            使用HiveContext的table方法可以直接去读取Hive数据仓库中的Table并生成DataFrame，
            接下来就可以进行机器学习、图计算、各种复杂的ETL等操作
         */
        val dataFromHive = hiveContext.table( "peopleInfomationResult" )
        dataFromHive.show





    }

}
