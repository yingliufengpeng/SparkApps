package com.dt.spark.sql

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by peng.wang on 2016/4/16.
  */
object SparkSQLWindowFuntionOPs {
    def main(args: Array[String]) {

//        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        val conf = new SparkConf().setMaster( "spark://Master:7077" ).setAppName("SparkSQLWindowFuntionOPs")
        val sc = new SparkContext(conf)
        //        val sqlContext = new SQLContext(sc)
        /*
            第一：在目前企业级大数据Spark开发的时候，绝大数情况下是采用Hive作为数据仓库
                Spark提供了Hive的支持功能，Spark通过HiveContext可以直接操作Hive中的数据
                基于HiveContext我们可以使用sql/hql两种方式来编写SQL语句对Hive进行操作，包括
                创建表、删除表、往表里导入数据以及用SQL语法构造各种SQL语法构造各种SQL语句对
                表中的数据进行CRUD（增删改查）操作
            第二：我们也可以直接通过aveAsTAble的方式把DataFrame中的数据保存到Hive数据仓库中
            第三：可以直接通过HIveContext.table方法来直接加载Hive中的表二生成DataFrame

         */
        val hiveContext = new HiveContext( sc )

        /*
          如果要创建的表如果存在的话，就会删除，然后创建我们要导入数据的表
       */
        hiveContext.sql( "use hive" ) //使用名称为hive的数据库，我们接下来所有的表的操作都会位于这个库中

        hiveContext.sql( "DROP TABLE IF EXISTS scores " )

        hiveContext.sql( "CREATE TABLE IF NOT EXISTS scores( name STRING , score INT )"
            + "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\n'" )

        hiveContext.sql( "LOAD DATA INPATH '/root/data/topNGroup.txt' INTO table scores " )

        //把要处理的数据导入到Hive表中

        /*
            使用子查询的方式完成目标数据的提取，在目标数据内幕使用窗口函数row_number来进行分组排序
            PARTITION BY：指定窗口函数分组的key
            ORDER BY:分组后进行排序
         */

        val result = hiveContext.sql("SELECT name,score "
            + "FROM ("
                + "SELECT "
                + "name,"
                + "score,"
                + "row_number() OVER (PARTITION BY name ORDER BY score DESC) rank"
                +" FROM scores "
                + ") sub_scores "
            + "WHERE rank <=4"  )

        result.show //在Driver的控制台上打印结果内容

        hiveContext.sql( "DROP TABLE IF EXISTS sortedResultScores " )
        result.saveAsTable( "sortedResultScores" )

    }
}
