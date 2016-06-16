package com.dt.spark.sparkstreaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
  * Created by peng.wang on 2016/4/19.
  */
object OnlineBBSUserLogs {




    def main(args: Array[String]) {
        System.setProperty("hadoop.home.dir", "E:\\centos6.5\\Big_data\\hadoop-2.6.4\\hadoop-2.6.4" )
        /*
            第一步：配置SparkConf：
            1，至少有2条线程：因为Spark Streaming应用程序在运行的时候，至少有一条
            线程用用于不断的训话结束数据，并且至少有一条线程用于处理接受数据（否则的话
            无法有线程用于处理数据，随着时间的推移，内存和磁盘都会不堪重负）
            2，对于集群而言，每个Executor一般的肯定不止一个线程，那对于处理Spark Streaming
            的应用程序而言，每个Executor一般分配多少Core比较合适？根据我们过去的经验，5个
            左右的Core是最佳的（一个段子：Core分配为奇数个Core表现最佳，例如3个、5个、7个Core等）
         */
        val conf = new SparkConf().setMaster( "local[4]" ).setAppName( "OnlineBBSUserLogs" )

        /*
            第二步：创建SparkStreamingContext：
            1，这个是SparkStreaming应用程序所有功能的起始点和程序调度的核心
            SparkStreamingContext的构建可以基于SparkConf参数，也可基于持久化的SparkStreamingContext
            的内容恢复过来（典型的场景是Driver崩溃后重新启动，由于Spark Streaming具有连续7*24小时不
            间断运行的特征，所有需要在Driver重新启动后继续上一次的状态，此时的状态恢复需要基于曾经的
            Checkpoint）
            2，在一个Spark Straming的应用程序中可以创建若干个SparkStreamingContext对象，使用下一个SparkStreaming
            之前就需把前面正在运行的SparkSteamingContext对象关闭掉，由此，我们获得一个重大的启发，
            SparkStreaming也只是Spark Core上的一个应用程序而已，只不过SparkStreaming框架想要运行的话，
            只需要工程需写业务逻辑处理代码
         */
        val streamContext = new StreamingContext( conf , Durations.seconds( 10 ) )

        /*
            第三步：创建SparkStreaming输入数据来源input Stream：
            1，数据输入来源可以基于File、HDFS、Flume、Kafka、Socket等
            2，在这里我们指定数据来源于网络Socket端口，Spark Streaming连接上该端口并在运行的时候一直监听
            该端口的数据（当然该端口服务首先必须存在），并且在后续会根据业务需要不断的有数据产生（当然对
            与SparkStreaming应用程序乐颜，有无数据其处理流程都是一样的）
            3，如果经常在每间隔5秒钟没有数据的话，会不断的启动空的Job，其实是会造成调度资源的浪费，因为并
            没有数据需要发生计算：
                实例的企业级生产环境的代码在具体提交Job前，会判断时候有数据，如果没有的话，就不在提交Job

         */


//        val lines = streamContext.socketTextStream( "Master" , 9999 )
        val lines = KafkaUtils.createDirectStream[ String , String , StringDecoder , StringDecoder]( streamContext ,

            Map( "metadata.broker.list" -> "Master:9092,Worker:9092" ) ,
            Set( "UserLogs" )
        )

        //在线PV计算
        onlinePV(lines)

        //在线UV计算
        onlineUV(lines)

        //在线Registered计算
        onlineRegistered(lines)

        //在线计算跳出率
        onlineJumed(lines)

        //在线计算不同模块的PV
        onlineChannelPV(lines)

        /*
            SparkStreaming执行引擎也就是Driver开始运行，Driver启动的时候是位于一条新的线程中，当然其部
            有消息循环体，接受应用应用程序本身后者Executor中的消息

         */

        streamContext.start

        streamContext.awaitTermination

        streamContext.stop()


    }

    def onlineChannelPV(lines: InputDStream[(String, String)]) = {
        lines.map( pairs => {
            val logs = pairs._2.split( "\t" )
            val channelID = logs( 4 )
            ( channelID , 1L )
        }).reduceByKey( _ + _ ).print()
    }

    def onlineJumed(lines: InputDStream[(String, String)]) = {
        lines.filter( pairs => {
                val logs = pairs._2.split("\t")
                val action = logs(5)
                if ("Register".equals(action)) {
                    true
                } else {
                    false
                }
            }).map( pairs => {
            val logs = pairs._2.split( "\t" )
            val userID = if( logs( 2 ) != null ) logs( 2 ).toLong else -1
            ( userID , 1L )
        }).reduceByKey( _ + _ ).filter( pairs => {
            if ( 1L  ==  pairs._2 ) {
                true
            } else {
                false
            }
        }).print()
    }

    def onlineRegistered(lines: InputDStream[(String, String)]) = {
        lines.filter( pairs => {
                val logs = pairs._2.split("\t")
                val action = logs(5)
                if ("Register".equals(action)) {
                    true
                } else {
                    false
                }
            }).count().print()
    }

    /**
      * 因为要计算UV，所以需要获得同样的Page的不同User，这个时候就要重复操作，DStream中有disctinct吗？
      * 当然没有（截止到Spark1.6.1的时候，还没有该API），此时我们就需要求助于DStream魔术般的方法tranfrom
      * 操作，这样就实现了UserID的去重，进而就可以计算出UV了。
      *
      * @param lines
      */
    def onlineUV(lines: InputDStream[(String, String)]): Unit = {
        /*
            第四步：接下来就像对于RDD编程一样基于Dstream进行编程！！！原因是DStream是RDD产生的模板（或者是类），在Spark
            Stream发生计算前，其实质是把每个Batch的DStream的操作翻译成为对RDD的操作！！！
            对初始的DStream进行Transformation级别的处理，例如map、filter等高阶函数等的编程，来进行具体的数据计算

         */
        val logsDStream = lines.filter(
            pairs => {
                val logs = pairs._2.split("\t")
                val action = logs(5)
                if ("View".equals(action)) {
                    true
                } else {
                    false
                }
            }
        ) .map( tuple => {
            val logs = tuple._2.split( "\t" )
            val userID = if( logs( 2 ) != null ) logs( 2 ).toLong else -1
            val pageID = logs( 3 ).toLong
            pageID + " " + userID
        }).transform( rdd => {
            rdd.distinct()
        } ).map( t => {
            val logs = t.split("\t")
            val pageID = logs( 0 )
            (pageID, 1L)
        }).reduceByKey( _ + _ ).print()



        /*
            此处的print并不会直接触发Job的支持，因为现在一切都是Spark Streaming框架的控制之下，对于Spark Stream
            而言具体是否触发真正的Job运行时基于设置Duration来设置时间间隔

            诸位一定要注意的是SparkStreaming应用程序要执行具体的Job，对Dtream就必须有output Stram操作，
            output Stream有很多类型的函数触发，例如print、saveAsTextFile、saveAsHadoopFiles等，最为重
            要的一个方法是foreaechRDD，因为Spark Streaming处理的结果一般会放在Redis、DB、DashBoard等上面
            ，foreachRDD主要就是用来完成这些功能的，而且可以随意的自定义具体数据到底放到哪里

            在企业生产环境下，一般会把计算的数据放入到Redis或者DB中，采用J2EE等技术进行趋势的绘制等，这
            就像股票交易一样，实现在线的监控等
         */

    }

    def onlinePV(lines: InputDStream[(String, String)]): Unit = {
        /*
            第四步：接下来就像对于RDD编程一样基于Dstream进行编程！！！原因是DStream是RDD产生的模板（或者是类），在Spark
            Stream发生计算前，其实质是把每个Batch的DStream的操作翻译成为对RDD的操作！！！
            对初始的DStream进行Transformation级别的处理，例如map、filter等高阶函数等的编程，来进行具体的数据计算
            第4.1步：将每一行的字符串拆分成单个的单词
         */
        val logsDStream = lines.filter(
            pairs => {
                val logs = pairs._2.split("\t")
                val action = logs(5)
                if ("View".equals(action)) {
                    true
                } else {
                    false
                }
            }
        )

        /*
            第五步：对舒适的DStream进行Transformation级别的处理，例如map、filter等高阶函数等编程，来进行具体的数据
                的计算。
            第4.2步：在单词拆分的基础上对每个单词实例计数为1，也就是word => ( word , 1 )
         */
        val pairs = logsDStream.map(
            tuple => {
                val logs = tuple._2.split("\t")
                val pageID = logs(3).toLong
                (pageID, 1L)
            }
        )


        /*
            第四步：对初始化的DStream进行Transformation级别的处理，例如map、filter等高阶函数等的编程，来进行具体的
                数据计算
            第4.3步：在每个单词实例计数为1基础之上统计每个单词在文件中出现的总次数
         */
        val wordCounts = pairs.reduceByKey(_ + _)


        /*
            此处的print并不会直接触发Job的支持，因为现在一切都是Spark Streaming框架的控制之下，对于Spark Stream
            而言具体是否触发真正的Job运行时基于设置Duration来设置时间间隔

            诸位一定要注意的是SparkStreaming应用程序要执行具体的Job，对Dtream就必须有output Stram操作，
            output Stream有很多类型的函数触发，例如print、saveAsTextFile、saveAsHadoopFiles等，最为重
            要的一个方法是foreaechRDD，因为Spark Streaming处理的结果一般会放在Redis、DB、DashBoard等上面
            ，foreachRDD主要就是用来完成这些功能的，而且可以随意的自定义具体数据到底放到哪里

            在企业生产环境下，一般会把计算的数据放入到Redis或者DB中，采用J2EE等技术进行趋势的绘制等，这
            就像股票交易一样，实现在线的监控等
         */
        wordCounts.print()
    }
}
