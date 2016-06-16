package com.dt.spark.sparkstreaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.streaming.kafka.KafkaUtils

import org.apache.spark.streaming.{Seconds, StreamingContext}
import  kafka.serializer.StringDecoder
import org.apache.kafka.common.network.Send
import com.yammer.metrics.Metrics
/**
  *
  *
  * @author DT大数据梦工厂
  * 新浪微博：http://weibo.com/ilovepains/
  *

 */


case class  MessageItem( name : String , age : Int )

object SparkStreamingFromKafkaFlume2Hive {
   def main(args: Array[String]): Unit = {

//       if( args.length < 2 ) {
//           System.err.println( "Please input your kafka Broker List and topics to consume" )
//           System.exit( 1 )
//       }

     val conf = new SparkConf()
     conf.setAppName( "SparkStreamingFromKafkaFlume2Hive" )
//     conf.setMaster( "local[4]" )
     conf.setMaster( "spark://Master:7077" )
     

       val ssc = new StreamingContext( conf , Seconds( 5 ) )

//       val Array( brokers , topicList ) = args
//       val KafkaParms = Map[ String , String ]( "metadata.broker.list" -> brokers )
//       val topics = topicList.split( "," ).toSet


        val hottestStream = KafkaUtils.createDirectStream[ String , String , StringDecoder , StringDecoder]( ssc ,
             Map( "metadata.broker.list" -> "Master:9092,Worker:9092" ) ,
             Set( "HelloKafka" )
//       ).map( pair => (  pair._2.split( "," )( 0 ) , pair._2.split( "," )( 1 ) ) ).print()
        ).map( pair => pair._2.split( "," ) ).foreachRDD( rdd => {
            val hiveContext = new HiveContext( rdd.sparkContext )
            import hiveContext.implicits._
            rdd.map( record => ( record( 0 ).trim , record( 1 ).trim.toInt ) ).toDF()
                .registerTempTable( "temp" )

            hiveContext.sql( "select count( * ) from temp" ).show()
        })



       /**
         * 今天的作业
         *  1，把数据写入到Hive中
         *  2，通过Java技术访问Hive中的内容
         *  3，通过Flume做最原始的数据的收集
         *  4，Flume会作为Kafka的Producer把数据写入到Kafka中供本程序消费
        *
         */



     ssc.start()
     ssc.awaitTermination()
     
     
  }
}



