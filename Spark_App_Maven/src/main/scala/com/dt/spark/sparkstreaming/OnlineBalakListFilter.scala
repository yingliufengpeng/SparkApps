package com.dt.spark.sparkstreaming

import org.apache.spark.sql.catalyst.expressions.Second
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
    背景描述：在广告点击击飞系统中，我们在线过滤掉黑名单的点击，进而保护广告商的利益，只进行有效的广告点击
    或者在防刷屏评分（或者流量）系统，过滤掉无效的投票或者评分或者流量
    实现技术：使用transform API直接基于RDD编程，进行Join操作
 */



object OnlineBalakListFilter {
   def main(args: Array[String]): Unit = {
     val conf = new SparkConf()
     conf.setAppName( "OnlineBalakListFilter" )
     conf.setMaster( "local[4]" )
       //    conf.setMaster( "spark://Master:7077" )
     
//     val sc = new SparkContext( conf )

       val ssc = new StreamingContext( conf , Seconds( 5 ) )
     /**
        黑名单数据准备，实际上黑名单一般都是动态的，例如在Redis或者数据库中，黑名单的生成往往有
          复杂的逻辑，具体的情况算法不同，但是在Spark Streaming进行处理的时候每次都能够访问完整
          的信息
      */
     val blackList = Array( "hadoop" -> true , "mahout" -> true )

     val blackListRDD = ssc.sparkContext.parallelize( blackList , 8 )

     val adsClickStream = ssc.socketTextStream( "Master" , 9999 )
//     val lines = sc.textFile( "resources/README.md" )

       /**
            此处模拟的广告点击的每条数据的格式为：time、name
            此处map操作的结果是name、（time、name）的格式
        */
     
     val adsClickStreamFormatted = adsClickStream.map( ads => ( ads.split( " " )( 1 ) , ads ) )

     adsClickStreamFormatted.transform( userClickRDD =>
         {
             /**
                通过leftOuterJoin操作既保留了左侧用户广告点击内容的RDD的所有内容，又获得了相应点击内容是否
                在黑名单中
              */

             val joinedBlackListRDD = userClickRDD.leftOuterJoin( blackListRDD )

             /**
               * 进行filter过滤的时候，其输入元素是一个Tuple：（ name：（ （ time ， name） ， boolean ） ）
               * 其中第一个元素是黑名单的名称，第二个元素的第二个元素是进行leftOuterJoin的时候是否存在的值
               * 如果值存在的话，表明当前的广告是黑名单，需要过滤掉，否则的话则是有效点击的内容
               */
             val validClicked = joinedBlackListRDD.filter( joinedItem =>
                 {

                     if( joinedItem._2._2.getOrElse( false ) )
                     {
                         false
                     }
                     else
                     {
                         true
                     }

                 }
             )

             validClicked.map( validClick =>
                 {
                     validClick._2._1
                 }
             )
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



