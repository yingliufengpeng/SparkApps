package com.dt.spark.sparkstreaming

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Properties}

//import kafka.javaapi.producer.Producer
import kafka.producer.{Producer, KeyedMessage, ProducerConfig}



import scala.util.Random

/**
  * Created by peng.wang on 2016/4/18.
  *     论坛数据自动生成代码，该生成的数据会作为Producer的方式发送给Kafka，然后SparkStreming
  *     程序会从Kafka中在线Pull到论坛或者网站的论坛用户在线行为的信息，进而进行多维度的在线
  *     分析
  * 论坛数据自动生成，数据格式如下
  * date：日期，格式：yyyy-MM-dd
  * timestamp：时间戳
  * userID：用户ID
  * pageID：页面ID
  * chanelID：板块ID
  * action：点击和注册
  */

/**
  * @param topic  //发送给Kafka的数据的类别
 */
class SparkStreamingDataManuallyProducerForKafka( topic : String ) extends Thread {

    val conf = new Properties()
    conf.put( "metadata.broker.list" , "Master:9092,Worker:9092" )
    conf.put( "serializer.class" , "kafka.serializer.StringEncoder" )

    val producerForkafka = new Producer[ Int , String ]( new ProducerConfig( conf ) )

    override  def run: Unit = {
        var counter = 0
        while( true ) {
            counter += 1
            val userLog = SparkStreamingDataManuallyProducerForKafka.userLogs()
//            println( "product: " + userLog )
            producerForkafka.send(  new KeyedMessage[ Int , String ]( topic , userLog  ) )

            if( counter % 500 == 0 ){
                counter = 0
                Thread.sleep( 1000 )
            }
        }
    }


}


object SparkStreamingDataManuallyProducerForKafka{

    //启动一个新的线程
    def createSparkStreamingDataManuallyProducerForKafka( topic : String )  = {
        new SparkStreamingDataManuallyProducerForKafka( topic ).start()
    }

    //具体的论坛频道
    val channelNames = Array( "Spark" ,
        "Scala" , "Kafka" , "Flink" ,
        "Hadoop" , "Storm" , "Hive" ,
        "Impala" , "HBase"  , "ML" )

    //用户的两种行为模式
    val actionNames = Array( "View" , "Register" )

    var yesterdayFormated :String  = _

    def main(args: Array[String]) {
        val path = "resources/userLogs.txt"
        var topic = ""

        if( args.length > 0 ) {

            topic = args( 0 )
        }

        yesterdayFormated = yesterday

        /**
          * 启动一个新的线程来发送数据,发送给Kafka集群，当然，发送给的话题是UserLogs，
          *
          * kafka的topic的创建
          * ./bin/kafka-topics.sh --create --zookeeper Master:2181,Worker:2181 --replication-factor 1
          *     --partitions 1 --topic UserLogs
          *
          *  kakfa的消费者的创建
          *  ./bin/kafka-console-consumer.sh --zookeeper Master:2181,Worker:2181 --topic UserLogs
          */

        createSparkStreamingDataManuallyProducerForKafka( "UserLogs" )


    }

    def userLogs( ): String = {
        val random = new Random()
        val userLogBuffer = new StringBuffer( "" )
        val unregisterdUsers = 1 to 8

         {

            val timestamp = new Date().getTime
            var userID = 0L
            var pageID = 0L

            if( unregisterdUsers( random.nextInt( 8 ) ) == 1 ) {
                userID = 0L
            }else {
                userID = random.nextInt( 200 )
            }

            //随机生成的页面ID
            pageID = random.nextInt( 200 )

            //随机生成的Channel
            val channel = channelNames( random.nextInt( 10 ) )

            //随机生成action行为
            val action = actionNames( random.nextInt( 2 ) )

            userLogBuffer.append( yesterdayFormated)
                .append("\t")
                .append(timestamp)
                .append("\t")
                .append(userID)
                .append("\t")
                .append(pageID)
                .append("\t")
                .append(channel)
                .append("\t")
                .append(action)
//                .append("\n")

             return userLogBuffer.toString

        }


    }
    def yesterday : String = {
        val date = new SimpleDateFormat( "yyyy-MM-dd" )
        val cal = Calendar.getInstance()
        cal.setTime( new Date() )
        cal.add( Calendar.DATE , 0 )
        val yesterday = cal.getTime
        date.format( yesterday )
    }
}