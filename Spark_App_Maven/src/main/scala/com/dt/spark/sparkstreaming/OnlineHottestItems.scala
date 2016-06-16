package com.dt.spark.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Scala开发集群运行的Spark来在线热搜索词
  * @author DT大数据梦工厂
  * 新浪微博：http://weibo.com/ilovepains/
  *
  * 背景描述：在社交网络（例如微博）、电子商务（例如京东）、搜索引擎（例如百度）等人们核心关注的
  * 内容之一就是我们所关注的内容中大家正在最关注的什么或者说当前的热点是什么，这在实际企业应用中
  * 是非常有价值的。例如我们关系过去的30分钟大家正在热搜索什么，
  *
  * 每5分钟更新一次，这就使得热点内容是动态更新，当然也是更有价值
  * 实现技术：Spark Streaming提供了滑动窗口的技术来支撑实现上述业务背景，我们可以使用reduceByKeyAndWindow
  * 操作来做具体的实现

 */



object OnlineHottestItems {
   def main(args: Array[String]): Unit = {
     val conf = new SparkConf()
     conf.setAppName( "OnlineHottestItems" )
     conf.setMaster( "local[4]" )
//     conf.setMaster( "spark://Master:7077" )
     
//     val sc = new SparkContext( conf )

       /**
         * 此处设置Bachch Interval是在Spark Streaming中生成基本的Job的时间单位，窗口和滑动的
         * 时间间隔一定是改Batch Interval的整数倍
         */
       val ssc = new StreamingContext( conf , Seconds( 1 ) )
     /**
            黑名单数据准备，实际上黑名单一般都是动态的，例如在Redis或者数据库中，黑名单的生成往往有
            复杂的逻辑，具体的情况算法不同，但是在Spark Streaming进行处理的时候每次都能够访问完整
            的信息
      */




     val hottestStream = ssc.socketTextStream( "Master" , 9999 )
//     val lines = sc.textFile( "resources/README.md" )


       /**
         * 用户的搜索格式简化为name、item，，在这里我们只需要计算出热点内容，所以只需要
         * 提取出item即可，提取出的item然后通过map转换为（ item ， 1 ）格式
         */
     val searchPairDStream = hottestStream.map( _.split( " " )( 1 ) ).map( ( _ , 1 ) )

     val hottestDStream = searchPairDStream.reduceByKeyAndWindow( ( v1 : Int  , v2 : Int  ) => v1 + v2 , Seconds( 5 ) , Seconds( 2 ) )

     hottestDStream.transform( hottestItemRDD =>
        {
            val top3 = hottestItemRDD.map( pair => ( pair._2 , pair._1 ) ).sortByKey( false ).map( pair => ( pair._2 , pair._1  ) ).take( 3 )
//            for( item <- top3 )
//                println( item )
            top3.foreach( println )
            hottestItemRDD

        }
     ).print



       /**
         * 计算后的数据一般都会写入到Kafka中，下游的计费系统会从Kafka中pull到有效数据进行计费
         */
       //     val pairs = words.map{ word => ( word , 1 ) }

     ssc.start()
     ssc.awaitTermination()
     
     
  }
}



