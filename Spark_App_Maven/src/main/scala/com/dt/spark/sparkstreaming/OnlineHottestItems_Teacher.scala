package com.dt.spark.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Scala开发集群运行的Spark来实现在线热搜索词
  *
  * @author DT大数据梦工厂
  * 新浪微博：http://weibo.com/ilovepains/
  *
  * 背景描述：在社交网络（例如微博）、电子商务（例如京东）、搜索引擎（例如百度）等人们核心关注的内容之一就是我所关注的内容中
  * 大家正在最关注什么或者说当前的热点是什么，这在实际企业级应用中是非常有价值的。例如我们关系过去30分钟大家正在热搜索什么，并且
  * 每5分钟更新一次，这就使得热点内容是动态更新，当然也是更有价值。
  * 实现技术：Spark Streaming提供了滑动窗口的技术来支撑实现上述业务背景，我们可以使用reduceByKeyAndWindow操作来做具体实现
  *
  */
object OnlineHottestItems_Teacher {
    def main(args: Array[String]){
        /**
          * 第1步：创建Spark的配置对象SparkConf，设置Spark程序的运行时的配置信息，
          * 例如说通过setMaster来设置程序要链接的Spark集群的Master的URL,如果设置
          * 为local，则代表Spark程序在本地运行，特别适合于机器配置条件非常差（例如
          * 只有1G的内存）的初学者       *
          */
        val conf = new SparkConf() //创建SparkConf对象
        conf.setAppName("OnlineHottestItems_Teacher") //设置应用程序的名称，在程序运行的监控界面可以看到名称
        conf.setMaster("spark://Master:7077") //此时，程序在Spark集群

        /**
          * 此处设置Batch Interval是在Spark Streaming中生成基本Job的时间单位，窗口和滑动时间间隔
          * 一定是该Batch Interval的整数倍
          */
        val ssc = new StreamingContext(conf, Seconds(5))


        val hottestStream = ssc.socketTextStream("Master", 9999)

        /**
          * 用户搜索的格式简化为 name item，在这里我们由于要计算出热点内容，所以只需要提取出item即可
          * 提取出的item然后通过map转换为（item，1）格式
          */
        val searchPair = hottestStream.map(_.split(" ")(1)).map(item => (item, 1))
        val hottestDStream = searchPair.reduceByKeyAndWindow( ( v1 : Int, v2 : Int ) => v1 + v2, Seconds( 30 ), Seconds( 10 ) )

        hottestDStream.transform( hottestItemRDD => {
            val top3 = hottestItemRDD.map( pair => ( pair._2 , pair._1) ).sortByKey( false ).
                map(pair => (pair._2, pair._1)).take(3)

            for(item <- top3){
                println(item)
            }

            hottestItemRDD
        }).print()

        ssc.start()
        ssc.awaitTermination()

    }
}