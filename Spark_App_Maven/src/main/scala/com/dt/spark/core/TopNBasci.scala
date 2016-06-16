package com.dt.spark.core

import org.apache.spark.{SparkContext, SparkConf}

 /*
  * 基础Top N案例实战
  * @author DT 大数据梦工厂 && 王家林 && 王鹏
  * 新浪微博：http://weibo.com/ilovepains
  */
object TopNBasci
{
    def main(args: Array[String]) {
        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
         /*
            创建Spark的配置对象SparkConf，设置Spark程序的运行时的配置信息，
            例如说通过setMaster来设置程序要链接Spark集群的Master的URL，如果
            设置为local，则代表Spark程序在本地运行，特地适合于机器配置条件非
            常差（例如只有1G的内存）的学者
          */
        val conf = new SparkConf() //创建SparkConf对象

        conf.setAppName( "Top N Basically " ) //设置应用程序的名称，在程序运行的监控界面可以看到名称
        conf.setMaster("local")


        /*
            第二步：创建SparkContext对象
            SparkContext是Spark程序所有功能的唯一入口，无论是采用Scala，Java，Python，R等都必有要有一个SparkContext
            SparkContext核心作用：初始化Spark应用程序运行时所需要的核心组件，包括DAGScheduler、TaskScheduler、SchedulerBackend
            同时还会负责Spark程序向Master注册程序等
            SparkContext是整个Saprk应用程序中最为重要的一个对象
         */

        val sc = new SparkContext( conf )  //创建SparkContext对象，通过传入SparkConf实例来定制运行的具体步骤参数和配置信息
        sc.setLogLevel( "WARN" )

        /*
            第三步：根据具体的数据来源（HDFS、HBase、Local、FS、DB、S3等）通过SparkContext创建RDD
            RDD的创建基本有三种方式，根据外部的数据来源（例如HDFS）、根据Scala集合、由其它的RDD操作
            数据会被RDD划分成为一系列的Partitions，分配到每个Partition的数据属于一个Task的处理范畴
         */

        val lines = sc.textFile( "resources/basicTopN.txt" )
                                                                    //读取本地数据文件并设置一个Partition
        val pairs = lines.map( line => ( line.toInt , line ) ) //生成key-Value键值对以方便sortByKey进行排序
        val sortedPairs = pairs.sortByKey( false )              //降序排序
        val sortedData = sortedPairs.map( pair => pair._2 )     //过滤出排序的内容本身
        val top5 = sortedData.take( 5 )                         //获取排名前5位的元素内容，元素内容构建成为一个Array
        top5.foreach( println )
        sc.stop
    }
}
